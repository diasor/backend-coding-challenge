package com.engage.codetest.api;

/**
 * @author Diana Sormani
 * Created: February 04, 2018
 * Last Updated: February 05, 2018
 * Description: The CurrencyError class was created to store the information ERROR provided by the "apilayer" rest endpoint for
 *              obtaining the convertion rates betweeen different currencies. Its memmbers are named
 *              exactly as they are defined in the endpoint: apilayer.
 */
public class CurrencyError {
    public String info;
    public int code;
}
