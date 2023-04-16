package com.ftn.RedditClone.lucene.indexing.handlers;

import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;

public class Word2007Handler extends DocumentHandler {

	public CommunityElastic getIndexUnitCommunity(File file) {
		CommunityElastic retVal = new CommunityElastic();

		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);

			String text = we.getText();
			retVal.setDescriptionFromFile(text);

			POIXMLProperties props = wordDoc.getProperties();

			String title = props.getCoreProperties().getTitle();
			retVal.setTitleFromFile(title);

			String keywords = props.getCoreProperties()
						.getUnderlyingProperties().getKeywordsProperty().get();

			retVal.setKeywords(keywords);

			retVal.setFilename(file.getCanonicalPath());

			we.close();

		} catch (Exception e) {
			System.out.println("Problem pri parsiranju docx fajla");
		}

		return retVal;
	}

	@Override
	public PostElastic getIndexUnitPost(File file) {
		PostElastic retVal = new PostElastic();

		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);

			String text = we.getText();
			retVal.setDescriptionFromFile(text);

			POIXMLProperties props = wordDoc.getProperties();

			String title = props.getCoreProperties().getTitle();
			retVal.setTitleFromFile(title);

			String keywords = props.getCoreProperties()
					.getUnderlyingProperties().getKeywordsProperty().get();

			retVal.setKeywords(keywords);

			retVal.setFilename(file.getCanonicalPath());

			we.close();

		} catch (Exception e) {
			System.out.println("Problem pri parsiranju docx fajla");
		}

		return retVal;
	}

	@Override
	public String getText(File file) {
		String text = null;
		try {
			XWPFDocument wordDoc = new XWPFDocument(new FileInputStream(file));
			XWPFWordExtractor we = new XWPFWordExtractor(wordDoc);
			text = we.getText();
			we.close();
		}catch (Exception e) {
			System.out.println("Problem pri parsiranju docx fajla");
		}
		return text;
	}

}
