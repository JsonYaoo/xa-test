package com.jsonyao.ttc.db142.model;

import java.math.BigDecimal;

public class AccountA {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_a.id
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_a.name
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column account_a.balance
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    private BigDecimal balance;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_a.id
     *
     * @return the value of account_a.id
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_a.id
     *
     * @param id the value for account_a.id
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_a.name
     *
     * @return the value of account_a.name
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_a.name
     *
     * @param name the value for account_a.name
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column account_a.balance
     *
     * @return the value of account_a.balance
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column account_a.balance
     *
     * @param balance the value for account_a.balance
     *
     * @mbg.generated Mon Feb 15 14:34:42 CST 2021
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}