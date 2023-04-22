package com.ftn.RedditClone.lucene.indexing.handlers;

import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import org.apache.lucene.document.DateTools;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class PDFHandler extends DocumentHandler {

	@Override
	public CommunityElastic getIndexUnitCommunity(File file) {
		CommunityElastic retVal = new CommunityElastic();
		try {
			PDFParser parser = new PDFParser((RandomAccessRead) new RandomAccessFile(file, "r"));
			parser.parse();
			String text = getText(parser);
			retVal.setDescriptionFromFile(text);

			// metadata extraction
			PDDocument pdf = parser.getPDDocument();
			PDDocumentInformation info = pdf.getDocumentInformation();

			String title = ""+info.getTitle();
			retVal.setTitleFromFile(title);

			String keywords = ""+info.getKeywords();
			retVal.setKeywords(keywords);
			
			retVal.setFilename(file.getCanonicalPath());
			
			//String modificationDate= DateTools.dateToString(new Date(file.lastModified()), DateTools.Resolution.DAY);

			pdf.close();
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}

		return retVal;
	}

	@Override
	public PostElastic getIndexUnitPost(File file) {
		PostElastic retVal = new PostElastic();
		try {
			PDFParser parser = new PDFParser((RandomAccessRead) new RandomAccessFile(file, "r"));
			parser.parse();
			String text = getText(parser);
			retVal.setDescriptionFromFile(text);

			// metadata extraction
			PDDocument pdf = parser.getPDDocument();
			PDDocumentInformation info = pdf.getDocumentInformation();

//			String title = ""+info.getTitle();
//			retVal.setTitle(title);

			String keywords = ""+info.getKeywords();
			retVal.setKeywords(keywords);

			retVal.setFilename(file.getCanonicalPath());

			String modificationDate= DateTools.dateToString(new Date(file.lastModified()), DateTools.Resolution.DAY);

			pdf.close();
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}

		return retVal;
	}

	@Override
	public String getText(File file) {
		try {
			PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
			parser.parse();
			PDFTextStripper textStripper = new PDFTextStripper();
			return textStripper.getText(parser.getPDDocument());
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}
		return null;
	}
	
	public String getText(PDFParser parser) {
		try {
			PDFTextStripper textStripper = new PDFTextStripper();
			return textStripper.getText(parser.getPDDocument());
		} catch (IOException e) {
			System.out.println("Greksa pri konvertovanju dokumenta u pdf");
		}
		return null;
	}

}
