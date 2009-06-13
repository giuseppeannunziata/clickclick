package net.sf.clickclick.examples.jquery.domain;

import java.util.Date;

public class Customer {

    private Boolean active;
    private Integer age;
    private Date dateJoined;
    private String email;
    private Double holdings;
    private String investments;
    private String name;
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public Integer getAge() {
        return age;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
    public Date getDateJoined() {
        return dateJoined;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setHoldings(Double holdings) {
        this.holdings = holdings;
    }

    public Double getHoldings() {
        return holdings;
    }

    public void setInvestments(String investments) {
        this.investments = investments;
    }

    public String getInvestments() {
        return investments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
