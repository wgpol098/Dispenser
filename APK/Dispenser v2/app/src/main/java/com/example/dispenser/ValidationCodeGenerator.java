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
        //Obliczanie sumy kontrolnej.
        String qrCode = String.valueOf(idDispenser);
        int sum=0;
        int control=124;
        char[] digits=qrCode.toCharArray();

        for(int i=0;i<qrCode.length();i++)
        {
            int number = Character.getNumericValue(digits[i]);
            number *= control*(i+1);
            if(number>999) number=number%1000;
            sum+=number;

            control+=9;
        }
        if(sum<1000) sum*=control;
        sum = sum % 1000;
        if(sum==0)
        {
            sum=control* qrCode.length() * (qrCode.length() % 10 + 10);
            sum = sum % 1000;
        }

        validationcode=sum;
    }

    public int getValidationCode()
    {
        return validationcode;
    }
}
