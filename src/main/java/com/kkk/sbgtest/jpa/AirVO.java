package com.kkk.sbgtest.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data                   //lombok
@NoArgsConstructor      //lombok
@AllArgsConstructor     //lombok
@Entity(name="tbl_weather_air5")
public class AirVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idx;

    @Column
    String sidoName;

/*
    @Column
    String stationName;

    @Column
    String dataTime;

    @Column
    String o3Value;

    @Column
    String o3Grade;

    @Column
    String pm10Value;

    @Column
    String pm10Grade;

    @Column
    String dongcode;

    @Column
    String so2Value;

    @Column
    String so2Grade;

    @Column
    String coValue;

    @Column
    String coGrade;

    @Column
    String no2Value;

    @Column
    String no2Grade;

    @Column
    String pm25Value;

    @Column
    String pm25Grade;

    @Column
    String khaiValue;

    @Column
    String khaiGrade;

    @Column
    String wthAnnounceTime;
*/

}
