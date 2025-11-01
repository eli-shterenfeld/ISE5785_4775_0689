# 3D Ray Tracing Rendering Engine (Java)

A modular ray-tracing engine built from scratch in Java. 
Supports realistic lighting, shadows, reflections, refractions, BVH acceleration, and perspective rendering.

## ✨ Features

- ✅ **Ray–Object Intersections**  
  Supports Sphere, Plane, Triangle, Polygon, Cylinder, Tube, Box and more.

- ✅ **Recursive Reflections & Refractions**  
  Full light transport simulation with Snell’s Law, Fresnel effect and configurable recursion depth.

- ✅ **Advanced Lighting & Shadows**  
  Phong illumination model, point and spot lights, shadows (including **soft shadows** using area light sampling).

- ✅ **BVH Acceleration Structure**  
  Bounding Volume Hierarchy implementation to reduce ray-scene intersection time and improve performance with thousands of objects.

- ✅ **Modular Architecture**  
  Clear separation into:
  - `geometry` – shapes and intersection logic  
  - `renderer` – ray tracer, pixel sampling, image writing  
  - `lighting` – materials, light sources, shading models  
  - `scene` – camera, objects, lights configuration

## 📷 Render Example

  ![Polygon Scene](stage2PolygonTreeTest_manualHierarchyCBRN.png)
