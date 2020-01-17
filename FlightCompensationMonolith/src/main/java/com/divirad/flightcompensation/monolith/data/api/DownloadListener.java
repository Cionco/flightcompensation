package com.divirad.flightcompensation.monolith.data.api;

import java.util.EventListener;

public interface DownloadListener extends EventListener {
	public void startingMultiDownload();
	public void dataDownloaded(DownloadEvent e);
	public void doneDownloading();
}
