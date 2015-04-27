package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;

/**
 * Utility methods for dealing with {@link OutputType}.
 */
public class OutputTypes {

	/**
	 * @param file
	 *            {@link File} that output will go to.
	 * @return {@link OutputType} that will send output to given file.
	 */
	public static OutputType<File> file(final File file) {
		return new OutputType<File>() {
			@Override
			public File convertFromBase64Png(String base64Png) {
				return save(BYTES.convertFromBase64Png(base64Png));
			}

			@Override
			public File convertFromPngBytes(byte[] data) {
				return save(data);
			}

			private File save(byte[] data) {
				OutputStream stream = null;

				try {
					stream = new FileOutputStream(file);
					stream.write(data);

					return file;
				} catch (IOException e) {
					throw new WebDriverException(e);
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							// Nothing sane to do
						}
					}
				}
			}
		};
	}

}
