package de.ju8co.nftpricemonitor;

import java.io.IOException;
import java.math.RoundingMode;
import java.util.Objects;

import com.google.common.math.DoubleMath;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TofuNft {

	private Double priceMax;
	private Double priceMin;
	private Long lockedValue;

	public Integer getNftCount() throws IOException {
		final Document document = Jsoup.connect(getUrl())
			.header("Accept-Encoding", "gzip, deflate")
			.userAgent("Mozilla")
			.timeout(100000)
			.get();

		final String pageHtml = document.outerHtml();
		final int start = pageHtml.indexOf("\"total\":") + 8;
		final int end = pageHtml.lastIndexOf("},\"activity\":");

		return Integer.parseInt(document.outerHtml().substring(start, end));
	}

	public TofuNft withMaxAvax(final Double priceMax) {
		Objects.requireNonNull(priceMax, "price can't be empty or null");
		this.priceMax = priceMax;

		return this;
	}

	public TofuNft withMinAvax(final Double priceMin) {
		Objects.requireNonNull(priceMin, "price can't be empty or null");
		this.priceMin = priceMin;

		return this;
	}

	public TofuNft withLockedValue(final Long lockedValue) {
		Objects.requireNonNull(lockedValue, "lockedValue can't be empty or null");
		this.lockedValue = lockedValue;

		return this;
	}

	public TofuNft withAutoCalculatedLockedValue() throws IOException {
		if (lockedValue != null) {
			throw new IllegalArgumentException("'withAutoCalculatedLockedValue' should not be used with 'withLockedValue' method");
		}

		final double univInUsd = new CoinGeckoPrice().withCurrency("usd").withCurrencyId("universe-2").getPrice();
		final double avaxInUsd = new CoinGeckoPrice().withCurrency("usd").withCurrencyId("avalanche-2").getPrice();

		lockedValue = DoubleMath.roundToLong(priceMax * avaxInUsd / univInUsd, RoundingMode.HALF_UP);

		return this;
	}

	public TofuNft withPercentage(final Integer percent, final Percentage percentage) {
		Objects.requireNonNull(lockedValue, "lockedValue is not set");

		if (Percentage.INCEASE.equals(percentage)) {
			lockedValue = DoubleMath.roundToLong(Double.valueOf(lockedValue) * ((double) (100 + percent) / 100), RoundingMode.HALF_UP);
		}

		if (Percentage.DECREASE.equals(percentage)) {
			lockedValue = DoubleMath.roundToLong(Double.valueOf(lockedValue) * ((double) (100 - percent) / 100), RoundingMode.HALF_UP);
		}

		return this;
	}

	public String getUrl() {
		return "https://tofunft.com/collection/universe-ecosystem/items?meta_double_0=" + lockedValue +
			",&priceMax=" + Objects.toString(priceMax, "") +
			"&priceMin=" + Objects.toString(priceMin, "");
	}
}