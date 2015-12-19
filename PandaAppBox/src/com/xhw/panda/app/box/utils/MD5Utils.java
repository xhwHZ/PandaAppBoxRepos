package com.xhw.panda.app.box.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils
{
	private MD5Utils()
	{
	}

	/**
	 * ʹ��md5���㷨���ı����м���
	 */
	public static String getTextMd5(String plainText)
	{
		byte[] secretBytes = null;
		try
		{
			secretBytes = MessageDigest.getInstance("md5").digest(
					plainText.getBytes());
		} catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException("NoSuchAlgorithmException");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);
		for (int i = 0; i < 32 - md5code.length(); i++)
		{
			md5code = "0" + md5code;
		}
		return md5code;
	}

	/**
	 * ��ȡ�ļ���MD5ֵ
	 */
	public static String getFileMd5(String filePath)
	{
		File file = new File(filePath);
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);

			byte[] buffer = new byte[1024];

			int len = -1;
			// ��ȡ������ժҪ
			MessageDigest messageDigest = MessageDigest.getInstance("md5");

			while ((len = fis.read(buffer)) != -1)
			{

				messageDigest.update(buffer, 0, len);

			}
			byte[] result = messageDigest.digest();

			StringBuffer sb = new StringBuffer();

			for (byte b : result)
			{
				int number = b & 0xff; // ���� +1 ;
				String hex = Integer.toHexString(number);
				if (hex.length() == 1)
				{
					sb.append("0" + hex);
				} else
				{
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally
		{
			if (fis != null)
			{
				try
				{
					fis.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				} finally
				{
					fis = null;
				}
			}
		}
	}
}
