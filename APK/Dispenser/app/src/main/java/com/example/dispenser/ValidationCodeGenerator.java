package com.example.dispenser;

public class ValidationCodeGenerator
{
    private int idDispenser;
    private int validationcode;

    public ValidationCodeGenerator(int _idDispenser)
    {
        idDispenser=_idDispenser;
    }

    public void Generate()
    {
        //Counting checksum
        int sum=0, control=124;
        char[] digits = String.valueOf(idDispenser).toCharArray();

        for(int i=0;i<digits.length;i++)
        {
            int number = Character.getNumericValue(digits[i]);
            number *= control*(i+1);
            number = number%1000;
            sum += number;
            control+=9;
        }
        if(sum<1000) sum*=control;
        sum = sum % 1000;
        if(sum==0) sum = (control* digits.length * (digits.length % 10 + 10)) % 1000;
        validationcode=sum;
    }

    public int getValidationCode()
    {
        return validationcode;
    }
}