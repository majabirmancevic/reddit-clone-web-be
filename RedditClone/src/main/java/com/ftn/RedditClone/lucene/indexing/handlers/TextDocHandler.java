package com.ftn.RedditClone.lucene.indexing.handlers;



import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TextDocHandler extends DocumentHandler {

	@Override
	public CommunityElastic getIndexUnitCommunity(File file) {
		CommunityElastic retVal = new CommunityElastic();
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, StandardCharsets.UTF_8));

			String firstLine = reader.readLine(); // u prvoj liniji svake
													// tekstualne datoteke se
													// nalazi naslov rada

			retVal.setTitleFromFile(firstLine);

			/*
			 * add other custom metadata
			 */

			String secondLine = reader.readLine();
			retVal.setKeywords(secondLine);

			StringBuilder fullText = new StringBuilder();
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText.append(" ").append(secondLine);
			}
			retVal.setDescriptionFromFile(fullText.toString());

			retVal.setFilename(file.getCanonicalPath());

			return retVal;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException ignored) {
				}
		}
	}

	@Override
	public PostElastic getIndexUnitPost(File file) {
		PostElastic retVal = new PostElastic();
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, StandardCharsets.UTF_8));

			String firstLine = reader.readLine(); // u prvoj liniji svake
			// tekstualne datoteke se
			// nalazi naslov rada

			retVal.setTitleFromFile(firstLine);

			/*
			 * add other custom metadata
			 */

			String secondLine = reader.readLine();
			retVal.setKeywords(secondLine);

			StringBuilder fullText = new StringBuilder();
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText.append(" ").append(secondLine);
			}
			retVal.setDescriptionFromFile(fullText.toString());

			retVal.setFilename(file.getCanonicalPath());

			return retVal;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException ignored) {
				}
		}
	}

	@Override
	public String getText(File file) {
		BufferedReader reader = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(
					fis, StandardCharsets.UTF_8));
			String secondLine;
			StringBuilder fullText = new StringBuilder();
			while (true) {
				secondLine = reader.readLine();
				if (secondLine == null) {
					break;
				}
				fullText.append(" ").append(secondLine);
			}
			return fullText.toString();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Datoteka ne postoji");
		} catch (IOException e) {
			throw new IllegalArgumentException("Greska: Datoteka nije u redu");
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch (IOException ignored) {
				}
		}
	}

}
