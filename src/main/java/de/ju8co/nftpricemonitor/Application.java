package de.ju8co.nftpricemonitor;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Application {

	public static void main(final String[] args) {
		start();
	}

	public static void start() {
		TofuNft tofuNft;
		int i = 0;
		do {
			try {
				Thread.sleep(20000);
				tofuNft = new TofuNft().withMinAvax(35.1).withMaxAvax(45.).withAutoCalculatedLockedValue().withPercentage(30, Percentage.DECREASE);

				final var found = tofuNft.getNftCount();
				if (found != 0) {
					System.out.println(tofuNft.getUrl() + " " + i++ + " ----------------------: " + found);

					final URL url = new URL("https://www.soundjay.com/buttons/beep-09.wav");
					final AudioClip clip = Applet.newAudioClip(url);
					clip.play();
				} else {
					System.out.println(tofuNft.getUrl() + " " + i++);
				}

			} catch (final Exception e) {
				System.err.println("Exception: " + e.getMessage());
			}

		} while (true);

	}

}