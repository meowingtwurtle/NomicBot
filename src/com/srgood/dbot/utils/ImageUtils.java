package com.srgood.dbot.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import static com.srgood.dbot.Reference.Numbers.VOTE_IMAGE_HEIGHT;
import static com.srgood.dbot.Reference.Numbers.VOTE_IMAGE_WIDTH;

public class ImageUtils {

    public static File renderVote(String voteName, String[] categoryNames, int[] categoryVotes) throws IOException {
        BufferedImage workImage = new BufferedImage(VOTE_IMAGE_WIDTH, VOTE_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        int totalVotes = IntStream.of(categoryVotes).sum();
        int degreesUsed = 0;
        Graphics graphics = workImage.getGraphics();
        graphics.drawString(voteName, VOTE_IMAGE_WIDTH / 4, VOTE_IMAGE_WIDTH / 8);
        for (int x : categoryVotes) {
            float degrees = ((float) x) / ((float) totalVotes);
            graphics.setColor(Color.WHITE);
            graphics.fillArc(VOTE_IMAGE_WIDTH / 2, VOTE_IMAGE_HEIGHT / 2, VOTE_IMAGE_WIDTH / 3, VOTE_IMAGE_HEIGHT / 3, degreesUsed, (int)degrees);
            degreesUsed += degrees;
        }
        graphics.dispose();
        workImage.flush();
        File file = File.createTempFile(SecureOverrideKeyGenerator.nextOverrideKey(), ".png");
        file.deleteOnExit();
        ImageIO.write(workImage, "png", file);
        return file;
    }
}
