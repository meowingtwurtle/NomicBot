package com.srgood.dbot.utils;

import com.srgood.dbot.Reference;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.stream.IntStream;

import static com.srgood.dbot.Reference.Numbers.VOTE_IMAGE_HEIGHT;
import static com.srgood.dbot.Reference.Numbers.VOTE_IMAGE_WIDTH;

public class ImageUtils {

    public static File renderVote(String voteName, String[] categoryNames, int[] categoryVotes) throws IOException {
        BufferedImage workImage = new BufferedImage(VOTE_IMAGE_WIDTH, VOTE_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        int colorInt = 0;
        int totalVotes = IntStream.of(categoryVotes).sum();
        float angle = 0;

        Graphics graphics = workImage.getGraphics();

        graphics.drawString(voteName, VOTE_IMAGE_WIDTH / 4, VOTE_IMAGE_WIDTH / 8);

        for (int i : categoryVotes) {
            float percent = (float) i / (float) totalVotes;
            float angleM = percent * 360;
            System.out.println("Slice percent: " + percent * 100 + " Slice angle: " + angleM + " Total Angle: " + angle);
            System.out.println(((int) angle + (int) angleM) + "  " + (int) angle);

            graphics.setColor(Reference.Strings.COLORS[colorInt]);
            graphics.fillArc(VOTE_IMAGE_WIDTH / 3 - 150,VOTE_IMAGE_HEIGHT / 2 - 150, 300,300, (int) angle,(int) angle + (int) angleM);

            angle += angleM;
            colorInt++;
        }

        graphics.dispose();
        workImage.flush();
        File file = File.createTempFile(SecureOverrideKeyGenerator.nextOverrideKey(), ".png");
        file.deleteOnExit();
        ImageIO.write(workImage, "png", file);
        return file;
    }

}
