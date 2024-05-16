package scoliosis;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static scoliosis.DrawCanvas.canvas;

public class Draw {

    static int drawDelay = 50;
    static float drawTicks = 0;

    static String nintendoCopyrightText = "best";

    static String gameboyText = "MUSHROOM";

    static Graphics g;

    static int gameboyAppearTime = 2;

    static boolean addedLetters = false;

    static ArrayList<BufferedImage> images = new ArrayList<>();

    public static void draw(BufferStrategy bs) {

        if (bs != null) {
            if (!addedLetters) {
                for (int i = 0; i < gameboyText.length(); i++) {
                    letterInfos.add(new LetterInfo(gameboyText.charAt(i), -1, -1));
                }

                addedLetters = true;
            }

            while (!done) {
                BufferedImage bi = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);

                g = bi.createGraphics();

                Graphics g2 = bs.getDrawGraphics();

                g.setColor(backgroundColor);
                g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

                int nintendoCopyrightFadeIn = doNintendoCopyrightFadeIn();

                if (nintendoCopyrightFadeIn > gameboyAppearTime) doGameboyLetters();

                images.add(bi);

                g2.drawImage(bi, 0, 0, canvas.getWidth(), canvas.getHeight(), null);

                g.dispose();
                bs.show();

                drawTicks+=0.3f;
            }

            System.out.println("we genning");
            genGifFromImages();
            System.exit(0);
        }
    }

    public static int doNintendoCopyrightFadeIn() {
        int alpha = (int) ((drawTicks) * 50f);
        alpha = Math.max(0, alpha);
        g.setColor(new Color(205, 41, 199, Math.min(alpha, 255)));
        g.setFont(fontForNintendo);
        g.drawString(nintendoCopyrightText, (canvas.getWidth()/2)-(g.getFontMetrics().stringWidth(nintendoCopyrightText)/2), canvas.getHeight()-(canvas.getHeight()/4));

        return alpha;
    }


    static int spaceBetweenGameboyChars = 720/gameboyText.length();
    static Font fontForGameboy = new Font("Sans Sheriff", Font.BOLD | Font.ITALIC, (int) (spaceBetweenGameboyChars*1.3f));
    static Font fontForNintendo = new Font("Sans Sheriff", Font.BOLD, 50);


    static Font placeholderfontDontRemoveX3 = new Font("Sans Sheriff", Font.BOLD, 50);

    static int animXoffset = 100;

    static float blehhhh = 2.5f;

    static boolean done = false;

    public static void doGameboyLetters() {
        for (int i = 0; i < gameboyText.length(); i++) {
            String charDraw = String.valueOf(gameboyText.charAt(i));
            float timeNumber = ((drawTicks)) - gameboyAppearTime;

            timeNumber -= i*0.3f;
            timeNumber -= 2;

            // use 150 because it doesnt do the whole sin rotation
            timeNumber = Math.min(2.5f, timeNumber);

            if (timeNumber > -1) {
                /* grrrrrr effort to make the x coordinate reverse
                if (i > gameboyText.length() / 2) {
                    double size = 3.5 - timeNumber;
                    int x = (int) (((((Math.sin(timeNumber) - Math.cos(timeNumber)) * 100) + (spaceBetweenGameboyChars * i * (Math.sqrt(size)))) - randomNumberIneedForOffsettingXinGameBoyText) + ((canvas.getWidth() / 2f) - (gameboyText.length() * (spaceBetweenGameboyChars / 2f)) + (randomNumberIneedForOffsettingXinGameBoyText / 2)));
                    int y = (int) (600 - (600 * Math.sin(timeNumber)));

                    // hops a bunch, check if hop done on each
                    //if (timeNumberUnCapped-(i/3f) >= 3.5f && timeNumberUnCapped-(i/3f) <= 4.5f)
                    //y += (int) (Math.sin(timeNumberUnCapped*7f + (i/3f)) * 20);


                    g.setFont(new Font(fontForGameboy.getFontName(), fontForGameboy.getStyle(), (int) (fontForGameboy.getSize() * size)));

                    g.drawString(charDraw, x, y);
                } else {
                 */
                double size = 5 - (timeNumber*Math.sqrt(timeNumber));

                g.setFont(placeholderfontDontRemoveX3);
                int x = (int) (Math.sin(timeNumber)-Math.cos(timeNumber));

                // offsets each x by their position
                x+= (int) (g.getFontMetrics().stringWidth(gameboyText.substring(0, i)) * (spaceBetweenGameboyChars/40F));
                // centres the text
                g.setFont(fontForGameboy);
                x-= (g.getFontMetrics().stringWidth(gameboyText) / 2);
                x+= (int) (canvas.getWidth()/2f);

                int y = (int) (770 - (600 * Math.sin(Math.sqrt(timeNumber))));

                if (letterInfos.get(i).hopTime == -1 && timeNumber >= blehhhh) {
                    letterInfos.get(i).hopTime = drawTicks;
                    blehhhh-=0.05f;
                }

                int delayBeforeHopTicks = 1;
                int hopTicks = 2;
                if (drawTicks - letterInfos.get(i).hopTime < hopTicks+delayBeforeHopTicks && drawTicks - letterInfos.get(i).hopTime > delayBeforeHopTicks) {
                    y -= (int) (Math.sin((drawTicks - letterInfos.get(i).hopTime - 1)*(3f/hopTicks))*15f);
                }


                if (letterInfos.get(i).hopTime != -1 && drawTicks - letterInfos.get(i).hopTime > hopTicks+delayBeforeHopTicks-4 && letterInfos.get(i).colorFadeTime == -1) {
                    letterInfos.get(i).colorFadeTime = drawTicks;
                }

                double timeNumberToLessThan1;
                int hslColor;
                Color color;

                if (timeNumber > 0) {
                    timeNumberToLessThan1 = (timeNumber / 2.5f);

                    hslColor = (int) (timeNumberToLessThan1 * (HSLcolors.length-1));

                    color = HSLcolors[hslColor];

                    if (hslColor != HSLcolors.length-1) {
                        Color newColor = HSLcolors[hslColor+1];

                        int difInRed = newColor.getRed()-color.getRed();
                        int difInGreen = newColor.getGreen()-color.getGreen();
                        int difInBlue = newColor.getBlue()-color.getBlue();

                        // yayy
                        double closeNessToWholeNumber = hslColor/(timeNumberToLessThan1 * (HSLcolors.length-1));

                        difInRed = (int) (difInRed * closeNessToWholeNumber);
                        difInGreen = (int) (difInGreen * closeNessToWholeNumber);
                        difInBlue = (int) (difInBlue * closeNessToWholeNumber);

                        color = new Color(newColor.getRed()-difInRed,newColor.getGreen()-difInGreen,newColor.getBlue()-difInBlue);
                    }
                }
                else {
                    color = HSLcolors[0];
                }


                int faderStart = 4;
                int faderTime = 4;
                if (letterInfos.get(i).colorFadeTime != -1 && drawTicks-letterInfos.get(i).colorFadeTime > faderStart && drawTicks-letterInfos.get(i).colorFadeTime < faderStart+faderTime) {
                    float fadeTime = drawTicks-letterInfos.get(i).colorFadeTime - faderStart;
                    fadeTime/=faderTime;

                    Color newColor = HSLcolors[0];

                    int difInRed = newColor.getRed()-color.getRed();
                    int difInGreen = newColor.getGreen()-color.getGreen();
                    int difInBlue = newColor.getBlue()-color.getBlue();

                    difInRed = (int) (difInRed * fadeTime);
                    difInGreen = (int) (difInGreen * fadeTime);
                    difInBlue = (int) (difInBlue * fadeTime);

                    color = new Color(color.getRed()+difInRed,color.getGreen()+difInGreen,color.getBlue()+difInBlue);
                }

                if (letterInfos.get(i).colorFadeTime != -1 && drawTicks-letterInfos.get(i).colorFadeTime > faderStart+faderTime && drawTicks-letterInfos.get(i).colorFadeTime < faderStart+faderTime+faderTime) {
                    float fadeTime = drawTicks-letterInfos.get(i).colorFadeTime - (faderStart+faderTime);
                    fadeTime/=faderTime;

                    Color newColor = HSLcolors[0];

                    int difInRed = newColor.getRed()-color.getRed();
                    int difInGreen = newColor.getGreen()-color.getGreen();
                    int difInBlue = newColor.getBlue()-color.getBlue();

                    difInRed = (int) (difInRed * fadeTime);
                    difInGreen = (int) (difInGreen * fadeTime);
                    difInBlue = (int) (difInBlue * fadeTime);

                    color = new Color(newColor.getRed()-difInRed,newColor.getGreen()-difInGreen,newColor.getBlue()-difInBlue);
                }


                g.setFont(new Font(fontForGameboy.getFontName(), fontForGameboy.getStyle(), (int) (fontForGameboy.getSize() * size)));
                g.setColor(color);

                g.drawString(charDraw, x, y);

                for (int n = 0; n < letterInfos.size(); n++) {
                    if (letterInfos.get(i).colorFadeTime != -1 && drawTicks-letterInfos.get(i).colorFadeTime > faderStart+faderTime+faderTime+faderStart) {
                        if (n == letterInfos.size()-1) {
                            done = true;
                        }
                    }
                    else break;
                }
            }
        }
    }

    public static ArrayList<LetterInfo> letterInfos = new ArrayList<>();


    // i think this is the order the gameboy follows
    static Color[] HSLcolors = {
            new Color(255, 0, 255),
            new Color(255, 0, 0),
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 255, 255),
            new Color(0, 0, 255),
    };

    static Color backgroundColor = new Color(29, 29, 29);

    public static String scoliosis = System.getenv("APPDATA") + "\\scoliosis";
    public static String baseName = scoliosis + "\\gameboyLogo";
    public static String resourcesFile = baseName + "\\resources";

    public static void genGifFromImages() {
        try {
            new File(resourcesFile).mkdirs();
            ImageOutputStream output = new FileImageOutputStream(new File(resourcesFile + "\\" + gameboyText + ".gif"));

            GifSequenceWriter writer = new GifSequenceWriter(output, BufferedImage.TYPE_4BYTE_ABGR, drawDelay, true);

            for (BufferedImage image : images) {
                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        if (image.getRGB(x,y) == backgroundColor.getRGB()) image.setRGB(x,y,new Color(0,0,0,0).getRGB());
                    }
                }
                writer.writeToSequence(image);
            }

            writer.close();
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // i like this effect i think its cool
    //double size = timeNumber-(Math.sqrt(timeNumber));
}
