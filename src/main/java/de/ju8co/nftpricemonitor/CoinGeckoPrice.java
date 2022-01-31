package de.ju8co.nftpricemonitor;

import java.io.IOException;
import java.util.Objects;

import com.google.gson.JsonParser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class CoinGeckoPrice {

	private String currency;
	private String currencyId;

	public CoinGeckoPrice withCurrency(String currency) {
		Objects.requireNonNull(currency, "currency should not be empty or null");
		this.currency = currency;

		return this;
	}

	public CoinGeckoPrice withCurrencyId(String currencyId) {
		Objects.requireNonNull(currency, "currencyId should not be empty or null");
		this.currencyId = currencyId;

		return this;
	}

	public Double getPrice() throws IOException {
		String price = JsonParser.parseString(getJson()).getAsJsonObject().get(currencyId).getAsJsonObject().get(currency).getAsString();

		return Double.parseDouble(price);
	}

	private String getJson() throws IOException {
		return Jsoup.connect(getUrl())
			.method(Connection.Method.GET)
			.ignoreContentType(true)
			.get()
			.text();
	}

	private String getUrl() {
		return "https://api.coingecko.com/api/v3/simple/price?ids=" + currencyId + "&vs_currencies=" + currency;
	}

}