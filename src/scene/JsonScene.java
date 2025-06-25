package scene;

import geometries.*;
import lighting.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import primitives.*;
import primitives.Vector;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * JsonScene class represents a scene in the 3D space and provides methods to import a scene from a JSON file.
 */
public class JsonScene {

    /**
     * Empty explicit default constructor to make javadoc generator happy
     */
    public JsonScene() {
    }

    /**
     * Imports a scene from a JSON file.
     *
     * @param path the path to the JSON file
     * @return the scene imported from the JSON file
     * @throws RuntimeException if there is an error reading the file or parsing the JSON
     */
    public static Scene importScene(String path) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(path));
            JSONObject sceneObj = (JSONObject) jsonObject.get("scene");

            String name = (String) sceneObj.get("name");
            Scene scene = new Scene(name);
            if (sceneObj.containsKey("background-color"))
                scene.setBackground(parseColor((String) sceneObj.get("background-color")));
            if (sceneObj.containsKey("ambient-light")) {
                JSONObject ambientLightObj = (JSONObject) sceneObj.get("ambient-light");
                Color ambientLight = parseColor((String) ambientLightObj.get("color"));
                scene.setAmbientLight(new AmbientLight(ambientLight));
            }
            if (sceneObj.containsKey("geometries")) {
                scene.setGeometries(parseGeometries((JSONArray) sceneObj.get("geometries")));
            }

            if (sceneObj.containsKey("lights"))
                scene.setLights(parseLights((JSONArray) sceneObj.get("lights")));

            return scene;
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to import scene from " + path, e);
        }
    }

    /**
     * Import lights from a JSON array.
     *
     * @param lights the JSON array containing light definitions
     * @return a list of LightSource objects parsed from the JSON array
     */
    private static List<LightSource> parseLights(JSONArray lights) {
        List<LightSource> lightSources = new LinkedList<>();
        for (Object obj : lights) {
            JSONObject lightObj = (JSONObject) obj;
            if (lightObj.containsKey("point")) {
                lightSources.add(parsePointLight((JSONObject) lightObj.get("point")));
            } else if (lightObj.containsKey("directional")) {
                lightSources.add(parseDirectionalLight((JSONObject) lightObj.get("directional")));
            } else if (lightObj.containsKey("spot")) {
                lightSources.add(parseSpotLight((JSONObject) lightObj.get("spot")));
            } else {
                throw new IllegalArgumentException("Unknown light type");
            }
        }
        return lightSources;
    }

    /**
     * Parses a spot light from a JSON object.
     *
     * @param lightObj the JSON object containing the spot light definition
     * @return a SpotLight object parsed from the JSON object
     */
    private static LightSource parseSpotLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("color"));
        Point position = parsePoint((String) lightObj.get("position"));
        Vector direction = parseVector((String) lightObj.get("direction"));
        SpotLight spotLight = new SpotLight(color, position, direction);
        if (lightObj.containsKey("kc")) {
            spotLight.setKc(((Number) lightObj.get("kc")).doubleValue());
        }
        if (lightObj.containsKey("kl")) {
            spotLight.setKl(((Number) lightObj.get("kl")).doubleValue());
        }
        if (lightObj.containsKey("kq")) {
            spotLight.setKq(((Number) lightObj.get("kq")).doubleValue());
        }
        if (lightObj.containsKey("narrow-beam")) {
            spotLight.setNarrowBeam((int) ((Number) lightObj.get("narrow-beam")).doubleValue());
        }
        if (lightObj.containsKey("size-of-light")) {
            spotLight.setAreaLightRadius(((Number) lightObj.get("size-of-light")).doubleValue(),
                    ((Number) lightObj.get("num-of-samples")).intValue());
        }
        return spotLight;
    }

    /**
     * Parses a directional light from a JSON object.
     *
     * @param lightObj the JSON object containing the directional light definition
     * @return a DirectionalLight object parsed from the JSON object
     */
    private static LightSource parseDirectionalLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("color"));
        Vector direction = parseVector((String) lightObj.get("direction"));
        return new DirectionalLight(color, direction);
    }

    /**
     * Parses a point light from a JSON object.
     *
     * @param lightObj the JSON object containing the point light definition
     * @return a PointLight object parsed from the JSON object
     */
    private static LightSource parsePointLight(JSONObject lightObj) {
        Color color = parseColor((String) lightObj.get("color"));
        Point position = parsePoint((String) lightObj.get("position"));
        PointLight pointLight = new PointLight(color, position);
        if (lightObj.containsKey("kc")) {
            pointLight.setKc(((Number) lightObj.get("kc")).doubleValue());
        }
        if (lightObj.containsKey("kl")) {
            pointLight.setKl(((Number) lightObj.get("kl")).doubleValue());
        }
        if (lightObj.containsKey("kq")) {
            pointLight.setKq(((Number) lightObj.get("kq")).doubleValue());
        }
        if (lightObj.containsKey("area-light-radius")) {
            pointLight.setAreaLightRadius(((Number) lightObj.get("area-light-radius")).doubleValue(),
                    ((Number) lightObj.get("num-of-samples")).intValue());
        }

        return pointLight;
    }

    /**
     * Parses geometries from a JSON array.
     *
     * @param geometriesArray the JSON array containing geometry definitions
     * @return a Geometries object containing all parsed geometries
     */
    private static Geometries parseGeometries(JSONArray geometriesArray) {
        Geometries geometries = new Geometries();
        for (Object obj : geometriesArray) {
            JSONObject geometryObj = (JSONObject) obj;
            Geometry geometry;
            if (geometryObj.containsKey("sphere")) {
                geometry = parseSphere((JSONObject) geometryObj.get("sphere"));
            } else if (geometryObj.containsKey("triangle")) {
                geometry = parseTriangle((JSONArray) geometryObj.get("triangle"));
            } else if (geometryObj.containsKey("plane")) {
                geometry = parsePlane((JSONObject) geometryObj.get("plane"));
            } else if (geometryObj.containsKey("polygon")) {
                geometry = parsePolygon((JSONArray) geometryObj.get("polygon"));
            } else if (geometryObj.containsKey("cylinder")) {
                geometry = parseCylinder((JSONObject) geometryObj.get("cylinder"));
            } else if (geometryObj.containsKey("tube")) {
                geometry = parseTube((JSONObject) geometryObj.get("tube"));
            } else {
                throw new IllegalArgumentException("Unknown geometry type");
            }

            if (geometryObj.containsKey("material"))
                geometry.setMaterial(parseMaterial((JSONObject) geometryObj.get("material")));

            if (geometryObj.containsKey("emission"))
                geometry.setEmission(parseColor((String) geometryObj.get("emission")));

            geometries.add(geometry);
        }
        return geometries;
    }

    /**
     * Parses a material from a JSON object.
     *
     * @param materialObj the JSON object containing the material definition
     * @return a Material object parsed from the JSON object
     */
    private static Material parseMaterial(JSONObject materialObj) {
        Material material = new Material();

        if (materialObj.containsKey("ka")) {
            material.setKA(parseDouble3(materialObj.get("ka")));
        }

        if (materialObj.containsKey("kd")) {
            material.setKD(parseDouble3(materialObj.get("kd")));
        }

        if (materialObj.containsKey("ks")) {
            material.setKS(parseDouble3(materialObj.get("ks")));
        }

        if (materialObj.containsKey("ns")) {
            material.setShininess(((Number) materialObj.get("ns")).intValue());
        }

        if (materialObj.containsKey("kt")) {
            material.setKT(parseDouble3(materialObj.get("kt")));
        }

        if (materialObj.containsKey("kr")) {
            material.setKR(parseDouble3(materialObj.get("kr")));
        }

        return material;
    }

    /**
     * Parses a tube from a JSON object.
     *
     * @param tube the JSON object containing the tube definition
     * @return a Tube object parsed from the JSON object
     */
    private static Geometry parseTube(JSONObject tube) {
        double radius = ((Number) tube.get("radius")).doubleValue();
        Ray axis = parseRay((JSONObject) tube.get("axis"));
        return new Tube(radius, axis);
    }

    /**
     * Parses a cylinder from a JSON object.
     *
     * @param cylinder the JSON object containing the cylinder definition
     * @return a Cylinder object parsed from the JSON object
     */
    private static Geometry parseCylinder(JSONObject cylinder) {
        double radius = ((Number) cylinder.get("radius")).doubleValue();
        double height = ((Number) cylinder.get("height")).doubleValue();
        Ray axis = parseRay((JSONObject) cylinder.get("axis"));
        return new Cylinder(radius, axis, height);
    }

    /**
     * Parses a ray from a JSON object.
     *
     * @param axis the JSON object containing the ray definition
     * @return a Ray object parsed from the JSON object
     */
    private static Ray parseRay(JSONObject axis) {
        Point point = parsePoint((String) axis.get("Head"));
        Vector direction = parseVector((String) axis.get("direction"));
        return new Ray(point, direction);
    }

    /**
     * Parses a polygon from a JSON array.
     *
     * @param polygon the JSON array containing the polygon vertices
     * @return a Polygon object parsed from the JSON array
     */
    private static Geometry parsePolygon(JSONArray polygon) {
        return new Polygon(parseVertices(polygon));
    }

    /**
     * Parses a sphere from a JSON object.
     *
     * @param sphereObj the JSON object containing the sphere definition
     * @return a Sphere object parsed from the JSON object
     */
    private static Geometry parseSphere(JSONObject sphereObj) {
        Point center = parsePoint((String) sphereObj.get("center"));
        double radius = ((Number) sphereObj.get("radius")).doubleValue();
        return new Sphere(radius, center);
    }

    /**
     * Parses a triangle from a JSON array.
     *
     * @param triangleObj the JSON array containing the triangle vertices
     * @return a Triangle object parsed from the JSON array
     */
    private static Geometry parseTriangle(JSONArray triangleObj) {
        Point[] points = parseVertices(triangleObj);
        return new Triangle(points[0], points[1], points[2]);
    }

    /**
     * Parses a plane from a JSON object.
     *
     * @param planeObj the JSON object containing the plane definition
     * @return a Plane object parsed from the JSON object
     */
    private static Geometry parsePlane(JSONObject planeObj) {
        Point point = parsePoint((String) planeObj.get("point"));
        Vector normal = parseVector((String) planeObj.get("normal"));
        return new Plane(point, normal);
    }

    /**
     * Parses an array of vertices from a JSON array.
     *
     * @param vertices the JSON array containing vertex definitions
     * @return an array of Point objects parsed from the JSON array
     */
    private static Point[] parseVertices(JSONArray vertices) {
        Point[] points = new Point[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            points[i] = parsePoint((String) vertices.get(i));
        }
        return points;
    }

    /**
     * Parses coordinates from a string and returns them as an array of doubles.
     *
     * @param coordStr the string containing coordinates separated by spaces
     * @return an array of doubles representing the coordinates
     */
    private static double[] parseCoordinates(String coordStr) {
        return Arrays.stream(coordStr.split(" "))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    /**
     * Parses a color from a string in the format "r g b".
     *
     * @param rgb the string containing RGB values
     * @return a Color object representing the parsed color
     */
    private static Color parseColor(String rgb) {
        double[] colors = parseCoordinates(rgb);
        return new Color(colors[0], colors[1], colors[2]);
    }

    /**
     * Parses a vector from a string in the format "x y z".
     *
     * @param vector the string containing vector coordinates
     * @return a Vector object representing the parsed vector
     */
    private static Vector parseVector(String vector) {
        double[] coords = parseCoordinates(vector);
        return new Vector(coords[0], coords[1], coords[2]);
    }

    /**
     * Parses a point from a string in the format "x y z".
     *
     * @param pointStr the string containing point coordinates
     * @return a Point object representing the parsed point
     */
    private static Point parsePoint(String pointStr) {
        double[] coords = parseCoordinates(pointStr);
        return new Point(coords[0], coords[1], coords[2]);
    }

    /**
     * Parses a Double3 from an object, which can be a Number or a String.
     *
     * @param obj the object to parse
     * @return a Double3 object representing the parsed value
     * @throws IllegalArgumentException if the object is not a valid type
     */
    private static Double3 parseDouble3(Object obj) {
        if (obj instanceof Number) {
            double val = ((Number) obj).doubleValue();
            return new Double3(val);
        } else if (obj instanceof String) {
            String[] parts = ((String) obj).trim().split("\\s+");
            if (parts.length == 1) {
                double val = Double.parseDouble(parts[0]);
                return new Double3(val);
            } else if (parts.length == 3) {
                return new Double3(
                        Double.parseDouble(parts[0]),
                        Double.parseDouble(parts[1]),
                        Double.parseDouble(parts[2])
                );
            } else {
                throw new IllegalArgumentException("Invalid Double3 format: " + obj);
            }
        } else {
            throw new IllegalArgumentException("Unsupported Double3 value type: " + obj.getClass());
        }
    }

}