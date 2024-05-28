package satisfy.wildernature.fabric.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import satisfy.wildernature.WilderNature;

@Config(name = WilderNature.MOD_ID)
public class ConfigFabric implements ConfigData {
    public boolean removeSavannaAnimals = true;
    public boolean removeSwampAnimals = true;
    public boolean removeForestAnimals = true;
    public boolean removeJungleAnimals = true;
    public boolean addJungleAnimals = true;
    public boolean spawnHazelnutBush = true;

    public int PelicanSpawnWeight = 5;
    public int DeerSpawnWeight = 12;
    public int RaccoonSpawnWeight = 8;
    public int SquirrelSpawnWeight = 8;
    public int RedWolfSpawnWeight = 10;
    public int OwlSpawnWeight = 12;
    public int BoarSpawnWeight = 14;
    public int BisonSpawnWeight = 10;
    public int TurkeySpawnWeight = 12;
}