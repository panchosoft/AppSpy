/*
 * Copyright (c) Panchosoft
 * Developed by Francisco I. Leyva
 * AppSpy - http://labs.panchosoft.com/appspy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.panchosoft.appspy.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Panchosoft
 */
public class iTunesApp {

    private String iTunesId;
    private String trackName;
    private String trackCensoredName;
    private String artworkUrl60;
    private String artworkUrl100;
    private String artistViewUrl;
    private String trackViewUrl;
    private String artistId;
    private String artistName;
    private String sellerName;
    private String price;
    private String sellerUrl;
    private String fileSizeBytes;
    private String releaseDate;
    private String contentAdvisoryRating;
    private String primaryGenreName;
    private String primaryGenreId;
    private String[] genres;
    private String[] genreIds;
    private String[] languageCodesISO2;
    private String[] supportedDevices;
    private String version;
    private String[] screenshotUrls;
    private String description;

    /**
     * @return the trackName
     */
    public String getTrackName() {
        return trackName;
    }

    /**
     * @param trackName the trackName to set
     */
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    /**
     * @return the trackCensoredName
     */
    public String getTrackCensoredName() {
        return trackCensoredName;
    }

    /**
     * @param trackCensoredName the trackCensoredName to set
     */
    public void setTrackCensoredName(String trackCensoredName) {
        this.trackCensoredName = trackCensoredName;
    }

    /**
     * @return the artworkUrl60
     */
    public String getArtworkUrl60() {
        return artworkUrl60;
    }

    /**
     * @param artworkUrl60 the artworkUrl60 to set
     */
    public void setArtworkUrl60(String artworkUrl60) {
        this.artworkUrl60 = artworkUrl60;
    }

    /**
     * @return the artworkUrl100
     */
    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    /**
     * @param artworkUrl100 the artworkUrl100 to set
     */
    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }

    /**
     * @return the artistViewUrl
     */
    public String getArtistViewUrl() {
        return artistViewUrl;
    }

    /**
     * @param artistViewUrl the artistViewUrl to set
     */
    public void setArtistViewUrl(String artistViewUrl) {
        this.artistViewUrl = artistViewUrl;
    }

    /**
     * @return the trackViewUrl
     */
    public String getTrackViewUrl() {
        return trackViewUrl;
    }

    /**
     * @param trackViewUrl the trackViewUrl to set
     */
    public void setTrackViewUrl(String trackViewUrl) {
        // Obtenemos el iTunesID de la aplicación
        // Obtenemos un Pattern con la expresión regular, y de él
        // un Matcher, para extraer los trozos de interés.
        // id284622652?
        Pattern patron = Pattern.compile("id([0-9]+)\\?");
        Matcher matcher = patron.matcher(trackViewUrl);

        // Hace que Matcher busque los trozos.
        matcher.find();

        if (matcher.groupCount() > 0) {
            this.setiTunesId(matcher.group(1));
        }


        this.trackViewUrl = trackViewUrl;
    }

    /**
     * @return the artistId
     */
    public String getArtistId() {
        return artistId;
    }

    /**
     * @param artistId the artistId to set
     */
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    /**
     * @return the artistName
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * @param artistName the artistName to set
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * @return the sellerName
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * @param sellerName the sellerName to set
     */
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return the sellerUrl
     */
    public String getSellerUrl() {
        return sellerUrl;
    }

    /**
     * @param sellerUrl the sellerUrl to set
     */
    public void setSellerUrl(String sellerUrl) {
        this.sellerUrl = sellerUrl;
    }

    /**
     * @return the fileSizeBytes
     */
    public String getFileSizeBytes() {
        return fileSizeBytes;
    }

    /**
     * @param fileSizeBytes the fileSizeBytes to set
     */
    public void setFileSizeBytes(String fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    /**
     * @return the releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate the releaseDate to set
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return the contentAdvisoryRating
     */
    public String getContentAdvisoryRating() {
        return contentAdvisoryRating;
    }

    /**
     * @param contentAdvisoryRating the contentAdvisoryRating to set
     */
    public void setContentAdvisoryRating(String contentAdvisoryRating) {
        this.contentAdvisoryRating = contentAdvisoryRating;
    }

    /**
     * @return the primaryGenreName
     */
    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    /**
     * @param primaryGenreName the primaryGenreName to set
     */
    public void setPrimaryGenreName(String primaryGenreName) {
        this.primaryGenreName = primaryGenreName;
    }

    /**
     * @return the primaryGenreId
     */
    public String getPrimaryGenreId() {
        return primaryGenreId;
    }

    /**
     * @param primaryGenreId the primaryGenreId to set
     */
    public void setPrimaryGenreId(String primaryGenreId) {
        this.primaryGenreId = primaryGenreId;
    }

    /**
     * @return the genres
     */
    public String[] getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    /**
     * @return the genreIds
     */
    public String[] getGenreIds() {
        return genreIds;
    }

    /**
     * @param genreIds the genreIds to set
     */
    public void setGenreIds(String[] genreIds) {
        this.genreIds = genreIds;
    }

    /**
     * @return the languageCodesISO2
     */
    public String[] getLanguageCodesISO2() {
        return languageCodesISO2;
    }

    /**
     * @param languageCodesISO2 the languageCodesISO2 to set
     */
    public void setLanguageCodesISO2(String[] languageCodesISO2) {
        this.languageCodesISO2 = languageCodesISO2;
    }

    /**
     * @return the supportedDevices
     */
    public String[] getSupportedDevices() {
        return supportedDevices;
    }

    /**
     * @param supportedDevices the supportedDevices to set
     */
    public void setSupportedDevices(String[] supportedDevices) {
        this.supportedDevices = supportedDevices;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the screenshotUrls
     */
    public String[] getScreenshotUrls() {
        return screenshotUrls;
    }

    /**
     * @param screenshotUrls the screenshotUrls to set
     */
    public void setScreenshotUrls(String[] screenshotUrls) {
        this.screenshotUrls = screenshotUrls;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the iTunesId
     */
    public String getiTunesId() {
        return iTunesId;
    }

    /**
     * @param iTunesId the iTunesId to set
     */
    public void setiTunesId(String iTunesId) {
        this.iTunesId = iTunesId;
    }

    @Override
    public String toString() {
        return this.getTrackName() + " (" + this.getVersion() + ")";
    }
}
