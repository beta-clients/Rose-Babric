package me.ht9.rose.util.config;

import com.google.gson.*;
import me.ht9.rose.Rose;
import me.ht9.rose.feature.gui.RoseGui;
import me.ht9.rose.feature.gui.component.impl.windows.ModuleWindow;
import me.ht9.rose.feature.registry.Registry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public final class FileUtils
{
    public static final File MAIN_FILE = new File("rose");
    public static final File MODULES_FILE = new File(MAIN_FILE, "modules");

    static
    {
        if (!MAIN_FILE.exists())
        {
            MAIN_FILE.mkdir();
        }
        if (!MODULES_FILE.exists())
        {
            MODULES_FILE.mkdir();
        }
    }

    public static void saveModules(File directory)
    {
        Registry.modules().forEach(m ->
        {
            try
            {
                JsonObject module = m.serialize();
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory, m.name().toLowerCase() + ".json")));
                bw.write(new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(module.toString())));
                bw.close();
            } catch (Throwable t)
            {
                Rose.logger().error("Failed to save module " + m.name() + ": ", t);
            }
        });
    }

    public static void loadModules(File directory)
    {
        Registry.modules().forEach(m ->
        {
            try
            {
                File modConfig = new File(directory, m.name().toLowerCase() + ".json");
                if (modConfig.exists())
                {
                    JsonObject module = JsonParser.parseReader(new FileReader(modConfig)).getAsJsonObject();
                    m.deserialize(module);
                }
            } catch (Throwable t)
            {
                Rose.logger().error("Failed to load module " + m.name() + ": ", t);
            }
        });
    }

    public static void saveClickGUI()
    {
        try
        {
            JsonObject clickGUI = new JsonObject();
            JsonArray windows = new JsonArray();
            for (ModuleWindow window : RoseGui.instance().windows())
            {
                JsonObject properties = new JsonObject();
                properties.add("name", new JsonPrimitive(window.getName()));
                properties.add("x", new JsonPrimitive(window.x()));
                properties.add("y", new JsonPrimitive(window.y()));
                properties.add("open", new JsonPrimitive(window.isOpened()));
                windows.add(properties);
            }
            clickGUI.add("windows", windows);

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(MAIN_FILE, "clickgui.json")));
            bw.write(new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(clickGUI.toString())));
            bw.close();
        } catch (Throwable t)
        {
            Rose.logger().error("Failed to save clickgui: ", t);
        }
    }

    public static void loadClickGUI()
    {
        try
        {
            File clickGUIConfig = new File(MAIN_FILE, "clickgui.json");
            if (clickGUIConfig.exists())
            {
                JsonObject clickGui = JsonParser.parseReader(new FileReader(clickGUIConfig)).getAsJsonObject();
                JsonArray clickguiArray = clickGui.get("windows").getAsJsonArray();
                for (ModuleWindow window : RoseGui.instance().windows())
                {
                    clickguiArray.forEach(element ->
                    {
                        JsonObject object = element.getAsJsonObject();
                        if (window.getName().equalsIgnoreCase(object.get("name").getAsString()))
                        {
                            window.setX(object.get("x").getAsFloat());
                            window.setY(object.get("y").getAsFloat());
                            window.setOpened(object.get("open").getAsBoolean());
                        }
                    });
                }
            }
        } catch (Throwable t)
        {
            Rose.logger().error("Failed to load clickgui: ", t);
        }
    }

//    public static void saveHudEditor(List<HudComponent> components)
//    {
//        try
//        {
//            JsonObject hudEditor = new JsonObject();
//            JsonArray elements = new JsonArray();
//            for (HudComponent component : components)
//            {
//                JsonObject properties = new JsonObject();
//                properties.add("name", new JsonPrimitive(component.hudElement().name()));
//                properties.add("x", new JsonPrimitive(component.x()));
//                properties.add("y", new JsonPrimitive(component.y()));
//                properties.add("enabled", new JsonPrimitive(component.hudElement().enabled()));
//                elements.add(properties);
//            }
//            hudEditor.add("elements", elements);
//
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(MAIN_FILE, "hudeditor.json")));
//            bw.write(new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(hudEditor.toString())));
//            bw.close();
//        } catch (Throwable t)
//        {
//            Rose.logger().error("Failed to save hudeditor: ", t);
//        }
//    }
//
//    public static void loadHudEditor(List<HudComponent> components)
//    {
//        try
//        {
//            File hudEditorConfig = new File(MAIN_FILE, "hudeditor.json");
//            if (hudEditorConfig.exists())
//            {
//                JsonObject hudEditor = JsonParser.parseReader(new FileReader(hudEditorConfig)).getAsJsonObject();
//                JsonArray elementsArray = hudEditor.get("elements").getAsJsonArray();
//                for (HudComponent component : components)
//                {
//                    elementsArray.forEach(element ->
//                    {
//                        JsonObject object = element.getAsJsonObject();
//                        if (component.hudElement().name().equalsIgnoreCase(object.get("name").getAsString()))
//                        {
//                            component.hudElement().setX(object.get("x").getAsFloat());
//                            component.hudElement().setY(object.get("y").getAsFloat());
//                            if (object.has("enabled"))
//                            {
//                                if (object.get("enabled").getAsBoolean())
//                                {
//                                    component.hudElement().toggle();
//                                }
//                            }
//                        }
//                    });
//                }
//            }
//        } catch (Throwable t)
//        {
//            Rose.logger().error("Failed to load hudeditor: ", t);
//        }
//    }
}