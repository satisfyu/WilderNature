package net.satisfy.wildernature.fabric.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.satisfy.wildernature.WilderNature;

@Config(name = WilderNature.MOD_ID)
public class ConfigFabric implements ConfigData {
    public boolean removeSavannaAnimals = true;
    public boolean removeSwampAnimals = true;
    public boolean removeJungleAnimals = true;
    public boolean removeForestAnimals = true;
    public boolean addJungleAnimals = true;
    public boolean spawnHazelnutBush = true;

    public int PelicanSpawnWeight = 5;
    public int PelicanMinGroupSize = 3;
    public int PelicanMaxGroupSize = 5;
    public int DeerSpawnWeight = 12;
    public int DeerMinGroupSize = 2;
    public int DeerMaxGroupSize = 4;
    public int RaccoonSpawnWeight = 8;
    public int RaccoonMinGroupSize = 2;
    public int RaccoonMaxGroupSize = 3;
    public int SquirrelSpawnWeight = 8;
    public int SquirrelMinGroupSize = 2;
    public int SquirrelMaxGroupSize = 2;
    public int RedWolfSpawnWeight = 10;
    public int RedWolfMinGroupSize = 2;
    public int RedWolfMaxGroupSize = 4;
    public int OwlSpawnWeight = 12;
    public int OwlMinGroupSize = 2;
    public int OwlMaxGroupSize = 3;
    public int BoarSpawnWeight = 14;
    public int BoarMinGroupSize = 4;
    public int BoarMaxGroupSize = 5;
    public int BisonSpawnWeight = 10;
    public int BisonMinGroupSize = 3;
    public int BisonMaxGroupSize = 6;
    public int TurkeySpawnWeight = 12;
    public int TurkeyMinGroupSize = 3;
    public int TurkeyMaxGroupSize = 5;
    public int DogSpawnWeight = 2;
    public int DogMinGroupSize = 1;
    public int DogMaxGroupSize = 1;
    public int MiniSheepSpawnWeight = 9;
    public int MiniSheepMinGroupSize = 3;
    public int MiniSheepMaxGroupSize = 6;
    public int PenguinSpawnWeight = 11;
    public int PenguinMinGroupSize = 4;
    public int PenguinMaxGroupSize = 6;
    public int CassowarySpawnWeight = 7;
    public int CassowaryMinGroupSize = 3;
    public int CassowaryMaxGroupSize = 5;
}