#include<LiquidCrystal.h>
LiquidCrystal lcd(13,12,11,10,9,8);
#define in 14
#define out 19
int num=0;
void ENTRY()
{
    num++;
    lcd.clear();
    lcd.print("Number of people in the bus:");
    lcd.setCursor(0,1);
    lcd.print(num);
    delay(1000);
}
void EXIT()
{
  num--;
    lcd.clear();
    lcd.print("Number of people in the bus:");
    lcd.setCursor(0,1);
    lcd.print(num);
    delay(1000);
}
void setup()
{
  lcd.begin(16,2);
  lcd.print("tally of people");
  delay(2000);
  pinMode(in, INPUT);
  pinMode(out, INPUT);
  lcd.clear();
  lcd.print("Number of people in the bus:");
  lcd.setCursor(0,1);
  lcd.print(num);
}
void loop()
{  
  
  if(digitalRead(in))
  ENTRY();
  if(digitalRead(out))
  EXIT();
  
  if(num<=0)
  {
    lcd.clear();
    lcd.clear();
    lcd.print("Bus is empty");
    lcd.setCursor(0,1);
    lcd.print("No bus service");
    delay(200);
  }
  
  
}
