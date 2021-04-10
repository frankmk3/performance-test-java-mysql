package com.test.performance.mysql.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class Report {

    @Id
    private String id;
    @Column
    private Date date;
    @Column
    private Date date1;
    @Column
    private Date date2;
    @Column
    private Date date3;
    @Column
    private Date date4;
    @Column
    private Date date5;
    @Column
    private Date date6;
    @Column
    private Date date7;
    @Column
    private String name;
    @Column(name = "group_name")
    private String group;
    @Column
    private String field1;
    @Column
    private String field2;
    @Column
    private String field3;
    @Column
    private String field4;
    @Column
    private String field5;
    @Column
    private String field6;
    @Column
    private Long numeric1;
    @Column
    private Long numeric2;
    @Column
    private Long numeric3;
    @Column
    private Long numeric4;
    @Column
    private Long numeric5;
    @Column
    private Long numeric6;
    @Column
    private Long numeric7;
    @Column
    private Long numeric8;
    @Column
    private Double double1;
    @Column
    private Double double2;
    @Column
    private Double double3;
    @Column
    private Double double4;
    @Column
    private Double double5;
    @Column
    private Double double6;
    @Column
    private Double double7;
    @Column
    private Double double8;
    @Column
    private boolean enabled;

}
