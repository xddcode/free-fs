package com.free.fs.common.utils;

import cn.hutool.core.codec.Base64;
import com.free.fs.common.exception.BusinessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/27 15:18
 */
@Slf4j
@Data
public class CaptchaUtil {
    //验证码个数
    private int count = 4;
    //验证码宽度，且设置每个字的宽度
    private int width = count * 50;
    //验证码高度
    private int height = 50;
    //图片验证码key
    private String code = "";
    //bufferedImage
    private BufferedImage bufferedImage;

    public BufferedImage getImage() {
        //图片缓冲区
        BufferedImage image = new BufferedImage(width, height, 1);
        //获得笔
        Graphics graphics = image.getGraphics();
        //设置初始画笔为白色
        graphics.setColor(new Color(255, 255, 254));
        //画满整个图，也就是把图片先变为白色
        graphics.fillRect(0, 0, width, height);
        Random rd = new Random();
        //设置字体
        Font font = new Font("宋体", Font.PLAIN, 35 + rd.nextInt(10));
        graphics.setFont(font);
        char[] chars = "qweCRYHrtasdfBxy678934VTGopNUFKuighjklzSXEDLOP12cvbnmQAZWJMI50".toCharArray();
        //画验证码
        for (int i = 0; i < count; i++) {
            String string = "";
            string += chars[rd.nextInt(chars.length)] + "";
            graphics.setColor(new Color(rd.nextInt(254), rd.nextInt(254), rd.nextInt(254)));
            graphics.drawString(string, 55 * i + rd.nextInt(10), 27 + rd.nextInt(15));
            code += string;
        }
        //干扰点
        for (int i = 0; i < 25 * count; i++) {
            graphics.setFont(new Font("宋体", Font.PLAIN, 15));
            String string = ".";
            graphics.setColor(new Color(rd.nextInt(255), rd.nextInt(255), rd.nextInt(255)));
            graphics.drawString(string, rd.nextInt(width), rd.nextInt(height));
        }
        //干扰线
        for (int i = 0; i < count + count / 2; i++) {
            graphics.setFont(new Font("宋体", Font.PLAIN, 10));
            graphics.setColor(new Color(rd.nextInt(255), rd.nextInt(255), rd.nextInt(255)));
            graphics.drawLine(rd.nextInt(width), rd.nextInt(height), rd.nextInt(width), rd.nextInt(height));
        }
        //归还笔
        graphics.dispose();
        //写到流里面需要用到ImageIo
        //这里做的测试，在本地测试下是否画的是那回事
          /*try {
              ImageIO.write(image,"jpg",new FileOutputStream("E:/11.jpg"));
          } catch (IOException e) {
              e.printStackTrace();
          }*/
        this.bufferedImage = image;
        return image;
    }

    public static String getBase64(BufferedImage image) {
        String base64 = null;
        try {
            //输出流
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            base64 = Base64.encode(stream.toByteArray());
            log.info("生成的图片验证码base64:{}", base64);
        } catch (IOException e) {
            log.error("生成生成的图片验证码base64失败：{}", e.getMessage());
            e.printStackTrace();
        }
        return base64;

    }

    /**
     * 验证码校验
     *
     * @param code
     * @param imgUUID
     */
    public static void verify(String code, String imgUUID) {
        String sourceCode = CaptchaCacheUtils.get(imgUUID);
        if (StringUtil.isBlank(sourceCode) || !sourceCode.equals(code)) {
            throw new BusinessException("验证码不正确");
        }
    }
}
