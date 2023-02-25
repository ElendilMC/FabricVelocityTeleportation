package dev.kugge.fabricvelocityteleportation.util;

import com.google.gson.Gson;
import net.minecraft.util.math.BlockPos;
import java.io.*;
import java.util.HashMap;

public class Database {
    private final String file;
    private HashMap<Range, String> data = new HashMap<>();

    public Database(String file) throws IOException {
        this.file = file;
    }

    public void add(BlockPos a, BlockPos b, String name) {
        Range range = new Range(a, b);
        this.data.put(range, name);
    }

    public void del(BlockPos a, BlockPos b) {
        Range range = new Range(a, b);
        this.data.remove(range);
    }

    public void load() throws IOException {
        File f = new File(this.file);
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
        BufferedReader reader = new BufferedReader(new FileReader(f));
        HashMap tempData = new Gson().fromJson(reader, this.data.getClass());
        if (!(tempData == null)) this.data = tempData;
        reader.close();
    }

    public void save() throws IOException {
        FileWriter writer = new FileWriter(this.file);
        writer.write(new Gson().toJson(this.data).toString());
        writer.close();
    }
}
