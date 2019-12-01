package com.yaapps.kikicare.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "animal_table")
public class Animal {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "race")
    private String race;
    //@ColumnInfo(name = "birth")
    @Ignore
    private Date birth;
    @ColumnInfo(name = "size")
    private String size;
    @ColumnInfo(name = "sexe")
    private String sexe;
    @ColumnInfo(name = "url_image")
    private String urlImage;
    @ColumnInfo(name = "id_user")
    private int iduser;
    @Ignore
    private int image;
    @Ignore
    private String birthDate;
    @ColumnInfo(name = "for_adoption")
    private int forAdoption;
    @ColumnInfo(name = "state")
    private int state;

public Animal(){};
    @Ignore
    public Animal(int id, String name, String sexe, int image) {
        this.id = id;
        this.name = name;
        this.sexe = sexe;
        this.image = image;
    }

    @Ignore
    public Animal(int id, String name, String sexe, String image) {
        this.id = id;
        this.name = name;
        this.sexe = sexe;
        this.urlImage = image;
    }

    public Animal(String name, String type, String race, String birthDate, String size, String sexe, String urlImage, int iduser) {
        this.name = name;
        this.type = type;
        this.race = race;
        this.size = size;
        this.sexe = sexe;
        this.urlImage = urlImage;
        this.iduser = iduser;
        this.birthDate=birthDate;
    }
    @Ignore
    public Animal(String name, String type, String race, Date birth, String size, String sexe, String urlImage, int iduser, int forAdoption, int state) {
        this.name = name;
        this.type = type;
        this.race = race;
        this.birth = birth;
        this.size = size;
        this.sexe = sexe;
        this.urlImage = urlImage;
        this.iduser = iduser;
        this.forAdoption = forAdoption;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getForAdoption() {
        return forAdoption;
    }

    public void setForAdoption(int forAdoption) {
        this.forAdoption = forAdoption;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}

