package com.yaapps.kikicare.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.yaapps.kikicare.Entity.Animal;

import java.util.List;


@Dao
public interface AnimalDao {
    @Query("SELECT * FROM animal_table")
    List<Animal> getAll();

    @Query("SELECT * FROM animal_table WHERE id IN (:animalIds)")
    List<Animal> loadAllByIds(int[] animalIds);

    @Query("SELECT * FROM animal_table WHERE id_user LIKE :id_user")
    List<Animal> findByUser(int id_user);

    @Query("SELECT * FROM animal_table WHERE for_adoption LIKE :adoption")
    List<Animal>  findForAdoption(int adoption);

    @Query("SELECT * FROM animal_table WHERE id == :id LIMIT 1")
    Animal findById(int id);

    @Insert
    void insertAll(Animal... animals);

    @Insert
    void insertOne(Animal animal);

    @Delete
    void delete(Animal animal);
}
