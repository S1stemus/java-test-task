import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.SplittableRandom;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        //переменная для проверки открытия файла
        boolean FileWasOpen=false;
        BufferedImage bufferedImage=null;
        //настройки команды mem
        HashMap<String,String>memSettings=new HashMap<String,String>();
        memSettings.put("-t",null);
        memSettings.put("-fn","Arial");
        memSettings.put("-fs","30");
        memSettings.put("-to","bottom");
        memSettings.put("-c","#FFFFFF");
        //настройки команды open
        HashMap<String,String> openSettings =new HashMap<String,String>();
        openSettings.put("-f","");
        //настройки команды save
        HashMap<String,String> saveSettings =new HashMap<String,String>();
        saveSettings.put("-f","");
        saveSettings.put("-ext","jpg");


        Scanner in = new Scanner((System.in));
        help();
        while (true){
            try{
                String LastParameter=null;
                int LastParameterIndex=0;
                String command=in.nextLine();
                String[] SplitCommand=command.split(" ");
                String commandType=SplitCommand[0];
                String[][]Settings=new String[SplitCommand.length-1][];
                for (int i=0;i<SplitCommand.length-1;i++) {
                    if(!SplitCommand[i+1].contains("=")){
                        Settings[LastParameterIndex][1]+=" "+SplitCommand[i+1];
                        continue;
                    }
                    Settings[i] = SplitCommand[i+1].split("=");
                    LastParameterIndex=i;
                }
                switch (commandType){
                    case "open":
                        for (String[] s:Settings) {
                            if(s==null){
                                continue;
                            }
                            openSettings.replace(s[0],s[1]);
                        }
                        bufferedImage=open(openSettings.get("-f"),FileWasOpen);
                        FileWasOpen=true;
                        break;
                    case "mem":
                        for (String[] s:Settings) {
                            if(s==null){
                                continue;
                            }
                            memSettings.replace(s[0],s[1]);
                        }
                        if (memSettings.get("-t")==null){
                            throw new IllegalArgumentException("Вы ничего не ввели в параметр -t. Ознакомьтесь с справкой ");
                        }
                        mem(bufferedImage,memSettings.get("-t"),memSettings.get("-c"),memSettings.get("-fn"),memSettings.get("-fs"),memSettings.get("-to"),FileWasOpen);
                        break;
                    case "save":
                        for (String[] s:Settings) {
                            saveSettings.replace(s[0],s[1]);
                        }
                        save(saveSettings.get("-f"),saveSettings.get("-ext"),bufferedImage,FileWasOpen);
                        FileWasOpen=false;
                        break;
                    case "quit":
                        return;
                    default:
                        System.out.println("Вы ввели неправильную команду");
                }
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }

        }
    }
    public static void mem(BufferedImage image,String Text,String color,String FontName,String FontSize,String TextOrientation,boolean FileWasOpen){
        try{
            if(!FileWasOpen){
                throw new IllegalArgumentException("выберите изображение перед тем, как создать мем");
            }
            Graphics2D g = image.createGraphics();
            g.setColor(Color.decode(color));
            g.setFont(new Font(FontName,Font.BOLD,Integer.parseInt(FontSize)));
            int height=image.getHeight();
            int width=image.getWidth();
            int TextWidth= g.getFontMetrics().stringWidth(Text);
            switch (TextOrientation) {
                case "top" -> g.drawString(Text, width / 2 - (TextWidth / 2), 75);
                case "centre" -> g.drawString(Text, width / 2 - (TextWidth / 2), height / 2);
                case "bottom" -> g.drawString(Text, width / 2 - (TextWidth / 2), height);
                default -> throw new IllegalArgumentException("Вы неправильно ввели параметр -to. Почитайте справку ");
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    public static BufferedImage open(String FileName,boolean FileWasOpen ){

        File file = new File(FileName);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return  image;
    }

    public static  void save (String FileName, String Ext,BufferedImage image,boolean FileWasOpen ){
        try {
            if(!FileWasOpen){
                throw  new IllegalArgumentException("Сначала выберите изображение");
            }
            ImageIO.write(image,Ext,new File(FileName));
            FileWasOpen=false;
        } catch (IOException e) {
            throw new RuntimeException("не удалось записать файл");
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    public static  void help(){
        var text= """
                Справка:
                Данное приложение предназначено для добавления текста к изображению
                Команды:
                help
                выводит все команды
                
                open -f
                выбор редактируемого изображения
                -f - путь к изображению.
                пример:  open -f=D:\\f\\image.jpg            
                
                mem -t [-fn [-fs]] [-c] [-to]  
                добавляет к картинке текст с определенным шрифтом.(не доступна пока не выполнена команда open)
                Аргументы:              
                -t - текст , который будет на изображении после исполнения команды.(Данный параметр обязательный)
                -c - цвет текста(принимает только хекс-код цвета).
                -fn - шрифт с которым текст будет написан.
                -fs- размер шрифта с которым текст будет написан.
                -to - располежение текста на изображении(top(сверху),centre(посередине),bottom(снизу))
                Пример:mem -t=я -c=#FFFFFF -to=top -fs=30
                
                save -f .(не доступна пока не выполнена команда open )
                Сохранение выбранного изображения
                -f- название сохраняемого файла(в пути файла укажите расширение , в котором будет хранится ваш файл)
                Пример:save -f=-f=D:\\f\\image.jpg  
                
                
                quit
                Завершение программы (желательно сохранять ваши картинки до выхода c помощью команды save)
                
                Совет: параметры после команды вводить черз пробел
                """;
        System.out.print(text);
    }
}