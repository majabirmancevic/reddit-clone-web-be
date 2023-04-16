package com.ftn.RedditClone.lucene.indexing.handlers;



import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;

import java.io.File;

public abstract class DocumentHandler {
	/**
	 * Od prosledjene datoteke se konstruise Lucene Document
	 * 
	 * @param file
	 *            datoteka u kojoj se nalaze informacije
	 * @return Lucene Document
	 */
	public abstract CommunityElastic getIndexUnitCommunity(File file);
	public abstract PostElastic getIndexUnitPost(File file);
	public abstract String getText(File file);

}
