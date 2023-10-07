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
        boolean FileWasOpen=false;
        BufferedImage bufferedImage=null;
        HashMap<String,String>memSettings=new HashMap<String,String>();
        memSettings.put("-t","mem");
        memSettings.put("-fn","Arial");
        memSettings.put("-fs","30");
        memSettings.put("-to","bottom");
        memSettings.put("-c","#000000");
        HashMap<String,String> openSettings =new HashMap<String,String>();
        openSettings.put("-f","");
        HashMap<String,String> saveSettings =new HashMap<String,String>();
        saveSettings.put("-f","");
        saveSettings.put("-ext","jpg");


        Scanner in = new Scanner((System.in));
        help();
        while (true){
            String command=in.nextLine();
            String[] SplitCommand=command.split(" ");
            String commandType=SplitCommand[0];
            String[][]Settings=new String[SplitCommand.length-1][];
            for (int i=0;i<SplitCommand.length-1;i++) {
                Settings[i] = SplitCommand[i+1].split("=");
            }
            switch (commandType){
                case "open":
                    for (String[] s:Settings) {
                        openSettings.replace(s[0],s[1]);
                    }
                    bufferedImage=open(openSettings.get("-f"),FileWasOpen);
                    FileWasOpen=true;
                    break;
                case "mem":
                    for (String[] s:Settings) {
                        memSettings.replace(s[0],s[1]);
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
            if(TextOrientation == "top"){
                g.drawString(Text,width/2-(TextWidth/2),75);
            } else if (TextOrientation == "center") {
                g.drawString(Text,width/2-(TextWidth/2),height/2);
            } else if (TextOrientation == "bottom") {
                g.drawString(Text,width/2-(TextWidth/2),height);
            } else {
                throw new IllegalArgumentException("Вы неправильно ввели параметр ");
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
                Команды:
                help
                выводит все команды
                
                open -f
                выбор редактируемого изображения
                -f - путь к изображению.
                пример: mem-application open -f:D:\\f\\image.jpg            
                
                mem -t [-fn [-fs]] [-c] [-to]  
                добавляет к картинке текст с определенным шрифтом.(не доступна пока не выполнена команда open)
                Аргументы:              
                -t - текст , который будет на изображении после исполнения команды.
                -c- цвет текста(принимает только хекс-код цвета).
                -fn - шрифт с которым текст будет написан.
                -fs- размер шрифта с которым текст будет написан.
                -to - располежение текста на изображении(
                
                save -f .(не доступна пока не выполнена команда open )
                Сохранение выбранного изображения
                -f- название сохраняемого файла(в пути файла укажите расширение , в котором будет хранится ваш файл)
                
                
                quit
                Завершение программы (желательно сохранять ваши картинки до выхода c помощью команды save)
                
                """;
        System.out.print(text);
    }
}