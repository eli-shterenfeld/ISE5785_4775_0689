package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;

/**
 * The {@code Geometries} class represents a collection of multiple {@link Intersectable} objects
 * with built-in BVH (Bounding Volume Hierarchy) functionality for accelerating ray-geometry intersections.
 * <p>
 * Each Geometries instance can be either:
 * - A leaf node (holding a small list of geometries)
 * - An internal node with two child Geometries nodes and a bounding AABB
 * <p>
 * Built recursively using SAH (Surface Area Heuristic) to choose
 * the optimal axis and split point for minimal traversal cost.
 * <p>
 * The class supports adding geometries dynamically and returns all intersection points,
 * or {@code null} if no intersections are found.
 *
 * @author eli and david
 */
public class Geometries extends Intersectable {

    /**
     * The infinite geometries which do not have bounding boxes.
     */
    private final List<Intersectable> infinite = new ArrayList<>();

    /**
     * The left child of this Geometries node. If this node is a leaf, this will be null.
     */
    private Geometries leftChild;

    /**
     * The right child of this Geometries node. If this node is a leaf, this will be null.
     */
    private Geometries rightChild;

    /**
     * A list of intersectable geometries contained in this node if it is a leaf node.
     */
    private List<Intersectable> leafGeometries;

    /**
     * A flag indicating whether this Geometries node is a leaf node or an internal node.
     */
    // private boolean isLeaf = true;
    private boolean isLeaf = false;
    /**
     * The bounding box of this Geometries node.
     * It is used for quick rejection of rays that do not intersect the geometries.
     */
    private boolean isRoot = false;

    /**
     * List of intersectable geometries (used before BVH construction).
     */
    private List<Intersectable> geometries = new ArrayList<>();

    /**
     * The maximum number of geometries that can be contained in a leaf node.
     */
    private static final int MAX_LEAF_SIZE = 6;

    /**
     * Precomputed comparators for sorting by each axis.
     */
    private static final List<Comparator<Intersectable>> AXIS_COMPARATORS = Arrays.asList(
            Comparator.<Intersectable>comparingDouble(g -> g.getBoundingBox().getCenter().getX()),
            Comparator.<Intersectable>comparingDouble(g -> g.getBoundingBox().getCenter().getY()),
            Comparator.<Intersectable>comparingDouble(g -> g.getBoundingBox().getCenter().getZ())
    );

    /**
     * Default constructor for the Geometries class.
     */
    public Geometries() {
    }

    /**
     * Constructs a Geometries object with a variable number of intersectable geometries.
     *
     * @param geometries the intersectable geometries to be added
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Private constructor for creating BVH leaf nodes.
     *
     * @param leafGeometries the list of geometries for this leaf node
     * @param isLeaf         flag indicating this is a leaf node
     */
    private Geometries(List<Intersectable> leafGeometries, boolean isLeaf) {
        this.leafGeometries = leafGeometries;
        this.isLeaf = isLeaf;
        setBoundingBox();
    }

    /**
     * Private constructor for creating BVH internal nodes.
     *
     * @param left  the left child node
     * @param right the right child node
     */
    private Geometries(Geometries left, Geometries right) {
        this.leftChild = left;
        this.rightChild = right;
        this.isLeaf = false;
        this.box = AABB.combine(left.getBoundingBox(), right.getBoundingBox());
    }

    /**
     * Adds a list of intersectable geometries to the current Geometries object.
     * Note: This only works before BVH construction.
     *
     * @param geometries the intersectable geometries to be added
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        List<Intersection> result = null;
        if (isRoot) {
            for (Intersectable item : infinite) {
                List<Intersection> tmp = item.calculateIntersections(ray, maxDistance);
                if (tmp != null) {
                    if (result == null)
                        result = new ArrayList<>(tmp);
                    else
                        result.addAll(tmp);
                }
            }
        }
        // If this is a BVH internal node
        if (leftChild != null && rightChild != null) {
            List<Intersection> leftResult = leftChild.calculateIntersections(ray, maxDistance);
            if (leftResult != null) {
                if (result == null) result = leftResult;
                else result.addAll(leftResult);
            }

            List<Intersection> rightResult = rightChild.calculateIntersections(ray, maxDistance);
            if (rightResult != null) {
                if (result == null) return rightResult;
                else result.addAll(rightResult);
            }
            return result;
        }

        for (Intersectable geo : (leafGeometries != null) ? leafGeometries : geometries) {
            List<Intersection> temp = geo.calculateIntersections(ray, maxDistance);
            if (temp != null) {
                if (result == null)
                    result = new ArrayList<>();
                result.addAll(temp);
            }
        }

        return result;
    }

    @Override
    public void setBoundingBox() {
        if (isLeaf) {
            this.box = computeBoundingBox(leafGeometries == null ? geometries : leafGeometries);
            return;
        }
        if (geometries.isEmpty()) {
            this.box = null;
            return;
        }

        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : geometries) {
            geo.setBoundingBox();
            AABB b = geo.getBoundingBox();
            if (b == null) continue;

            Point min = b.getMin(), max = b.getMax();
            if (min.getX() < minX) minX = min.getX();
            if (min.getY() < minY) minY = min.getY();
            if (min.getZ() < minZ) minZ = min.getZ();

            if (max.getX() > maxX) maxX = max.getX();
            if (max.getY() > maxY) maxY = max.getY();
            if (max.getZ() > maxZ) maxZ = max.getZ();
        }

        this.box = new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Builds a BVH acceleration structure from the current geometries.
     * Transforms this Geometries instance into a BVH root node.
     */
    public void buildBVH() {
        isLeaf = false;
        setBoundingBox();
        for (Intersectable g : geometries)
            if (g.getBoundingBox() == null) infinite.add(g);
        geometries.removeIf(g -> g.getBoundingBox() == null);
        Geometries root = buildBVHFrom(geometries);

        if (root.isLeaf) {
            this.leafGeometries = root.leafGeometries;
            this.isLeaf = true;
            this.leftChild = this.rightChild = null;
            this.box = null;
            ;
        } else {
            this.leftChild = root.leftChild;
            this.rightChild = root.rightChild;
            this.isLeaf = false;
            this.box = null;
            ;
        }
        this.isRoot = true; // Mark this as the root node of the BVH
        this.geometries.clear(); // Clear the original geometries list
    }

    /**
     * Builds a BVH from the given list of intersectable geometries.
     * This method recursively splits the geometries into left and right child nodes
     * based on the best split found using surface area heuristic.
     *
     * @param geometries the list of intersectable geometries to build the BVH from
     * @return a Geometries object representing the BVH structure
     */
    private Geometries buildBVHFrom(List<Intersectable> geometries) {
        if (geometries.size() <= MAX_LEAF_SIZE)
            return new Geometries(geometries, true); // Leaf node

        SplitResult split = findBestSplit(geometries);
        if (split == null || split.index <= 0 || split.index >= geometries.size())
            return new Geometries(geometries, true); // Fallback: make it a leaf

        geometries.sort(AXIS_COMPARATORS.get(split.axis));

        // Split the list into two parts
        Geometries left = buildBVHFrom(new ArrayList<>(geometries.subList(0, split.index)));
        Geometries right = buildBVHFrom(new ArrayList<>(geometries.subList(split.index, geometries.size())));

        return new Geometries(left, right);
    }

    /**
     * Computes the bounding box that contains all geometries in the list.
     *
     * @param list the list of intersectable geometries
     * @return a bounding box that encompasses all geometries in the list
     */
    private static AABB computeBoundingBox(List<Intersectable> list) {
        if (list == null || list.isEmpty()) return null;
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : list) {
            AABB b = geo.getBoundingBox();
            if (b == null) continue;

            Point min = b.getMin();
            Point max = b.getMax();

            minX = Math.min(minX, min.getX());
            minY = Math.min(minY, min.getY());
            minZ = Math.min(minZ, min.getZ());

            maxX = Math.max(maxX, max.getX());
            maxY = Math.max(maxY, max.getY());
            maxZ = Math.max(maxZ, max.getZ());
        }

        return new AABB(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Represents the result of a split operation.
     * Contains the axis of the split, the index where the split occurs,
     * and the cost associated with that split.
     */
    private static class SplitResult {

        /**
         * The axis along which the split occurs (0 = x, 1 = y, 2 = z).
         */
        final int axis;

        /**
         * The index in the sorted list of objects where the split occurs.
         */
        final int index;

        /**
         * Constructs a SplitResult with the specified axis, index, and cost.
         *
         * @param axis  the axis along which the split occurs (0 = x, 1 = y, 2 = z)
         * @param index the index where the split occurs in the sorted list of objects
         */
        public SplitResult(int axis, int index) {
            this.axis = axis;
            this.index = index;
        }
    }

    /**
     * Finds the best split for the given list of intersectable objects using surface area heuristic.
     * It evaluates splits along each axis (x, y, z) and chooses the one with the lowest cost.
     * Optimized to sort only once per axis instead of repeatedly sorting the same list.
     *
     * @param objects the list of intersectable objects to be split
     * @return a SplitResult containing the best axis, index, and cost for the split
     */
    private static SplitResult findBestSplit(List<Intersectable> objects) {
        SplitResult best = null;
        double bestCost = Double.POSITIVE_INFINITY;

        AABB globalBox = computeBoundingBox(objects);
        double totalArea = globalBox.surfaceArea();

        // Pre-sort the list for each axis to avoid repeated sorting
        List<List<Intersectable>> sortedLists = new ArrayList<>(3);
        for (int axis = 0; axis < 3; axis++) {
            List<Intersectable> sorted = new ArrayList<>(objects);
            sorted.sort(AXIS_COMPARATORS.get(axis));
            sortedLists.add(sorted);
        }

        for (int axis = 0; axis < 3; axis++) {
            List<Intersectable> sortedObjects = sortedLists.get(axis);
            int n = sortedObjects.size();

            // Precompute left and right bounding boxes
            AABB[] leftBoxes = new AABB[n];
            AABB[] rightBoxes = new AABB[n];

            AABB left = null;
            for (int i = 0; i < n; i++) {
                AABB b = sortedObjects.get(i).getBoundingBox();
                left = (left == null) ? new AABB(b) : left.combine(b);
                leftBoxes[i] = left;
            }

            AABB right = null;
            for (int i = n - 1; i >= 0; i--) {
                AABB b = sortedObjects.get(i).getBoundingBox();
                right = (right == null) ? new AABB(b) : right.combine(b);
                rightBoxes[i] = right;
            }

            // Try all split points
            for (int i = 1; i < n; i++) {
                AABB leftBox = leftBoxes[i - 1];
                AABB rightBox = rightBoxes[i];

                int leftCount = i;
                int rightCount = n - i;

                double leftArea = leftBox.surfaceArea();
                double rightArea = rightBox.surfaceArea();

                double cost = 1.0 + (leftCount * leftArea + rightCount * rightArea) / totalArea;

                if (cost < bestCost) {
                    bestCost = cost;
                    best = new SplitResult(axis, i);
                }
            }
        }
        return best;
    }
}