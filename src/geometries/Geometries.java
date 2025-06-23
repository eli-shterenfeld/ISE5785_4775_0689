package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.*;

/**
 * The {@code Geometries} class represents a collection of multiple {@link Intersectable} objects.
 * <p>
 * It allows grouping several geometries into a single composite structure
 * and provides functionality to find intersection points of a given {@link Ray}
 * with all the contained geometries.
 * <p>
 * The class supports adding geometries dynamically and returns all intersection points,
 * or {@code null} if no intersections are found.
 *
 * @author eli and david
 */
public class Geometries extends Intersectable {

    /**
     * List of intersectable geometries.
     */
    // private List<Intersectable> geometries = new LinkedList<Intersectable>();
    private List<Intersectable> geometries = new ArrayList<Intersectable>();

    private boolean isBVHBuilt = false;
    private Geometries leftChild = null;
    private Geometries rightChild = null;
    private static final int MAX_LEAF_SIZE = 6;

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
     * Adds a list of intersectable geometries to the current Geometries object.
     *
     * @param geometries the intersectable geometries to be added
     */
    public void add(Intersectable... geometries) {

        this.geometries.addAll(List.of(geometries));
    }

//    @Override
//    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
//        // System.out.println("Ray enter box: " + box);
//        List<Intersection> list = null;
//        for (Intersectable item : geometries) {
//            //  System.out.println("Checking item: " + item.getClass() + " @ " + System.identityHashCode(item));
//            List<Intersection> found = item.calculateIntersections(ray, maxDistance);
//            if (found != null) {
//                if (list == null)
//                    list = new LinkedList<>(found);
//                else
//                    list.addAll(found);
//            }
//        }
//        return list;
//    }

    @Override
    protected List<Intersection> calculateIntersectionsHelper(Ray ray, double maxDistance) {
        if (leftChild != null && rightChild != null) {
            List<Intersection> result = null;

            boolean leftHit = leftChild.box != null && leftChild.box.intersect(ray, maxDistance);
            boolean rightHit = rightChild.box != null && rightChild.box.intersect(ray, maxDistance);

            if (leftHit) {
                result = leftChild.calculateIntersectionsHelper(ray, maxDistance);
            }

            if (rightHit) {
                List<Intersection> rightResult = rightChild.calculateIntersectionsHelper(ray, maxDistance);
                if (rightResult != null) {
                    if (result == null) result = new ArrayList<>(rightResult);
                    else result.addAll(rightResult);
                }
            }

            return result;
        }

        // Leaf node – brute-force intersection with all geometries
        List<Intersection> result = null;
        for (Intersectable geo : geometries) {
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
        if (geometries.isEmpty()) {
            this.box = null;
            return;
        }

        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : geometries) {
            geo.setBoundingBox();
            Box b = geo.getBoundingBox();
            if (b == null) continue;

            Point min = b.getMin(), max = b.getMax();
            if (min.getX() < minX) minX = min.getX();
            if (min.getY() < minY) minY = min.getY();
            if (min.getZ() < minZ) minZ = min.getZ();

            if (max.getX() > maxX) maxX = max.getX();
            if (max.getY() > maxY) maxY = max.getY();
            if (max.getZ() > maxZ) maxZ = max.getZ();
        }

        this.box = new Box(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    /**
     * Builds a bounding volume hierarchy (BVH) from a list of intersectable geometries.
     * Uses a more efficient approach with surface area heuristic for better spatial partitioning.
     *
     * @return a list of intersectable geometries organized in a BVH
     */
    public void buildBVH() {
        if (isBVHBuilt || geometries.size() <= MAX_LEAF_SIZE) return;

        for (Intersectable geo : geometries)
            geo.setBoundingBox();

        SplitResult split = findBestSplit(geometries);
        if (split == null) return;
        if (split.index <= 0 || split.index >= geometries.size()) return;

        geometries.sort(Comparator.comparingDouble(g -> g.getBoundingBox().getCenter().get(split.axis)));

        List<Intersectable> leftList = new ArrayList<>(geometries.subList(0, split.index));
        List<Intersectable> rightList = new ArrayList<>(geometries.subList(split.index, geometries.size()));

        leftChild = new Geometries();
        leftChild.geometries = leftList;
        leftChild.setBoundingBox();
        leftChild.buildBVH();

        rightChild = new Geometries();
        rightChild.geometries = rightList;
        rightChild.setBoundingBox();
        rightChild.buildBVH();

        this.box = Box.combine(
                leftChild.getBoundingBox(),
                rightChild.getBoundingBox()
        );

        geometries.clear();
        isBVHBuilt = true;
    }

    private Box computeBoundingBox(List<Intersectable> list) {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY, minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY, maxZ = Double.NEGATIVE_INFINITY;

        for (Intersectable geo : list) {
            Box b = geo.getBoundingBox();
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

        return new Box(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
    }

    private static class SplitEvent {
        double position;
        boolean isStart; // true = min, false = max
        int objectIndex;

        public SplitEvent(double position, boolean isStart, int objectIndex) {
            this.position = position;
            this.isStart = isStart;
            this.objectIndex = objectIndex;
        }
    }

    private static class SplitResult {
        int axis;
        int index;
        double cost;

        public SplitResult(int axis, int index, double cost) {
            this.axis = axis;
            this.index = index;
            this.cost = cost;
        }
    }

    private SplitResult findBestSplit(List<Intersectable> objects) {
        SplitResult best = null;
        double bestCost = Double.POSITIVE_INFINITY;

        Box globalBox = computeBoundingBox(objects);
        double totalArea = globalBox.surfaceArea();

        for (int axis = 0; axis < 3; axis++) {
            List<SplitEvent> events = new ArrayList<>(objects.size() * 2);

            for (int i = 0; i < objects.size(); i++) {
                Box box = objects.get(i).getBoundingBox();
                events.add(new SplitEvent(box.getMin().get(axis), true, i));
                events.add(new SplitEvent(box.getMax().get(axis), false, i));
            }

            events.sort(Comparator.comparingDouble(e -> e.position));

            int leftCount = 0;
            int rightCount = objects.size();
            Box leftBox = null;
            Box rightBox = new Box(globalBox); // תוודא שיש לך copy constructor

            for (SplitEvent event : events) {
                Box b = objects.get(event.objectIndex).getBoundingBox();

                if (event.isStart) {
                    // move object from right to left
                    if (leftBox == null) leftBox = new Box(b);
                    else leftBox.expandToInclude(b);

                    leftCount++;
                    rightCount--;
                }

                if (leftCount == 0 || rightCount == 0) continue;

                double leftArea = leftBox.surfaceArea();
                double rightArea = rightBox.surfaceArea();

                double cost = 1.0 + (leftCount * leftArea + rightCount * rightArea) / totalArea;

                if (cost < bestCost) {
                    bestCost = cost;
                    best = new SplitResult(axis, event.objectIndex + 1, cost);
                }
            }
        }

        return best;
    }
}

