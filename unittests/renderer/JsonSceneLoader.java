package renderer;

import com.google.gson.*;
import geometries.*;
import lighting.*;
import primitives.*;
import scene.Scene;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonSceneLoader {

    public static Scene loadScene(String path) {
        Scene scene = new Scene("Scene from JSON");

        try (FileReader reader = new FileReader(path)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            // Ambient light
            if (root.has("ambientLight")) {
                JsonObject amb = root.getAsJsonObject("ambientLight");
                scene.setAmbientLight(new AmbientLight(parseColor(amb.get("color").getAsString())));
            }

            // Lights
            if (root.has("lights")) {
                JsonArray lights = root.getAsJsonArray("lights");
                for (JsonElement l : lights) {
                    JsonObject light = l.getAsJsonObject();
                    String type = light.get("type").getAsString();
                    Color color = parseColor(light.get("color").getAsString());

                    switch (type) {
                        case "SpotLight" -> scene.lights.add(
                                new SpotLight(
                                        color,
                                        parsePoint(light.get("position").getAsString()),
                                        parseVector(light.get("direction").getAsString())
                                ).setKl(light.get("kL").getAsDouble()).setKq(light.get("kQ").getAsDouble())
                        );
                        case "DirectionalLight" -> scene.lights.add(
                                new DirectionalLight(color, parseVector(light.get("direction").getAsString()))
                        );
                        // תוכל להוסיף גם PointLight אם צריך
                    }
                }
            }

            // Geometries
            JsonObject sceneJson = root.has("scene") ? root.getAsJsonObject("scene") : root;
            JsonArray geometries = sceneJson.getAsJsonArray("geometries");
            for (JsonElement e : geometries) {
                JsonObject geo = e.getAsJsonObject();
                Geometry g = parseGeometry(geo);
                if (g != null) scene.geometries.add(g);
            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot read scene file: " + path, e);
        }

        return scene;
    }

    private static Geometry parseGeometry(JsonObject geo) {
        try {
            if (geo.has("triangle")) {
                JsonArray pts = geo.getAsJsonArray("triangle");
                if (pts == null || pts.size() != 3)
                    throw new IllegalArgumentException("Invalid triangle: " + geo);
                return new Triangle(
                        parsePoint(pts.get(0).getAsString()),
                        parsePoint(pts.get(1).getAsString()),
                        parsePoint(pts.get(2).getAsString())
                ).setEmission(parseColorSafe(geo)).setMaterial(parseMaterialSafe(geo));
            }

            if (geo.has("sphere")) {
                JsonObject s = geo.getAsJsonObject("sphere");
                return new Sphere(
                        s.get("radius").getAsDouble(),
                        parsePoint(s.get("center").getAsString())
                ).setEmission(parseColorSafe(geo)).setMaterial(parseMaterialSafe(geo));
            }

            if (geo.has("plane")) {
                JsonObject p = geo.getAsJsonObject("plane");
                return new Plane(
                        parsePoint(p.get("point").getAsString()),
                        parseVector(p.get("normal").getAsString())
                ).setEmission(parseColorSafe(geo)).setMaterial(parseMaterialSafe(geo));
            }

            if (geo.has("polygon")) {
                JsonArray pts = geo.getAsJsonArray("polygon");
                if (pts == null || pts.size() < 3)
                    throw new IllegalArgumentException("Polygon with too few points: " + geo);

                List<Point> points = new ArrayList<>();
                for (int i = 0; i < pts.size(); i++) {
                    JsonElement p = pts.get(i);
                    if (p == null || p.isJsonNull())
                        throw new IllegalArgumentException("Null or missing point in polygon at index " + i + ": " + geo);
                    points.add(parsePoint(p.getAsString()));
                }

                return new Polygon(points.toArray(new Point[0]))
                        .setEmission(parseColorSafe(geo))
                        .setMaterial(parseMaterialSafe(geo));
            }

            if (geo.has("cylinder")) {
                JsonObject c = geo.getAsJsonObject("cylinder");
                return new Cylinder(
                        c.get("radius").getAsDouble(),
                        new Ray(parsePoint(c.get("origin").getAsString()), parseVector(c.get("direction").getAsString())),
                        c.get("height").getAsDouble()
                ).setEmission(parseColorSafe(geo)).setMaterial(parseMaterialSafe(geo));
            }

            if (geo.has("tube")) {
                JsonObject t = geo.getAsJsonObject("tube");
                return new Tube(
                        t.get("radius").getAsDouble(),
                        new Ray(parsePoint(t.get("origin").getAsString()), parseVector(t.get("direction").getAsString()))
                ).setEmission(parseColorSafe(geo)).setMaterial(parseMaterialSafe(geo));
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to parse geometry: " + geo);
            e.printStackTrace();
        }

        return null;
    }

    private static Color parseColorSafe(JsonObject geo) {
        return geo.has("emission") ? parseColor(geo.get("emission").getAsString()) : new Color(0, 0, 0);
    }

    private static Material parseMaterialSafe(JsonObject geo) {
        if (!geo.has("material")) return new Material();
        JsonObject m = geo.getAsJsonObject("material");
        Material mat = new Material();
        if (m.has("kd")) mat.setKD(m.get("kd").getAsDouble());
        if (m.has("ks")) mat.setKS(m.get("ks").getAsDouble());
        if (m.has("kt")) mat.setKT(m.get("kt").getAsDouble());
        if (m.has("kr")) mat.setKR(m.get("kr").getAsDouble());
        if (m.has("ns")) mat.setShininess(m.get("ns").getAsInt());
        return mat;
    }

    private static Point parsePoint(String s) {
        String[] p = s.trim().split("\\s+");
        return new Point(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
    }

    private static Vector parseVector(String s) {
        String[] v = s.trim().split("\\s+");
        return new Vector(Double.parseDouble(v[0]), Double.parseDouble(v[1]), Double.parseDouble(v[2]));
    }

    private static Color parseColor(String s) {
        String[] c = s.trim().split("\\s+");
        return new Color(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
    }
}
