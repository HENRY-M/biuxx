package com.biuxx.utils.security.cipher.holder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.biuxx.utils.security.cipher.SecurityCipherException;

public abstract class RSAFileHolderAdapter implements RSAFileHolder {

	private byte[] key = null;

	@Override
	public void init(String keypath) throws SecurityCipherException {
		File f = new File(keypath);
		if (!f.exists()) {
			throw new SecurityCipherException("File cannot be found!" + keypath);
		}

		try {
			this.key = readByte(f);
		} catch (Exception e) {
			throw new SecurityCipherException("Cannot be initialized!" + keypath);
		}
	}

	@Override
	public void init(byte[] keyBytes) throws SecurityCipherException {
		if (keyBytes == null || keyBytes.length < 0) {
			throw new SecurityCipherException("keyBytes cannot be empty!");
		}
		this.key = keyBytes;
	}

	@Override
	public InputStream newInputStream() {
		if (key == null) {
			throw new IllegalStateException("Not initialized!");
		}
		InputStream in = new ByteArrayInputStream(key);
		return in;
	}

	@Override
	public void releaseInputStream(InputStream keyStream) {
		if (keyStream != null) {
			try {
				keyStream.close();
			} catch (IOException e) {
			}
		}
	}

	private byte[] readByte(File f) throws IOException {
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
			}
			return byteBuffer.array();
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (channel != null) {
					channel.close();
				}
			} catch (IOException e) {
			}
			try {
				if (fs != null) {
					fs.close();
				}
			} catch (IOException e) {
			}
		}
	}
}
