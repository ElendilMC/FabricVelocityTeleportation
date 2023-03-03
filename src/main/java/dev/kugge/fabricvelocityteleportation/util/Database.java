package dev.kugge.fabricvelocityteleportation.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;import dev.kugge.fabricvelocityteleportation.FabricVelocityTeleportation;
import net.minecraft.util.math.BlockPos;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Database {
    private final String file;
    public HashMap<Range, Destination> data = new HashMap<>();

    public Database(String file) throws IOException {
        this.file = file;
    }

    public void add(BlockPos a, BlockPos b, String name, String destinationName) throws IllegalArgumentException {
        Range range = new Range(a, b);
        Destination destination = new Destination(name, destinationName);
        for (Range r: this.data.keySet()) if (r.equals(range)) throw new IllegalArgumentException("Key duplication");
        this.data.put(range, destination);
    }

    public void del(BlockPos a, BlockPos b) throws IllegalArgumentException {
        Range range = new Range(a, b);
        for (Range r: this.data.keySet()) {
            if (r.equals(range)) {
                this.data.remove(r);
                return;
            }
        }
        throw new IllegalArgumentException("Portal does not exists");
    }

    public void load() throws IOException {
        File f = new File(this.file);
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Type hashMapType = new TypeToken<HashMap<Range, Destination>>() {}.getType();
        HashMap<Range, Destination> tempData = new Gson().fromJson(reader, hashMapType);
        if (!(tempData == null)) this.data = tempData;
        reader.close();
        FabricVelocityTeleportation.LOGGER.info("Loaded portals !");
        FabricVelocityTeleportation.LOGGER.info(String.valueOf(tempData));
    }

    public void save() throws IOException {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(this.file);
        writer.write(gson.toJson(this.data));
        writer.close();
    }
}
