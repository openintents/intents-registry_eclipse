package org.openintents.intentsregistry.net;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.openintents.intentsregistry.IntentsregistryLog;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

// using SAX
public class IntentsregistryParser {
	class HowToHandler extends DefaultHandler {
		BufferedWriter out = null;
		boolean title = false;
		boolean oiAction   = false;
		public void startElement(String nsURI, String strippedName, String tagName, Attributes attributes)
				throws SAXException {
			if (tagName.equalsIgnoreCase("title"))
				title = true;
			if (tagName.equalsIgnoreCase("Action"))
				oiAction = true;			
		}

		@Override
		public void endDocument() throws SAXException {
			try {
				out.close();
			} catch (IOException e) {
				IntentsregistryLog.logError(e);
			}
		}

		@Override
		public void startDocument() throws SAXException {
			try {
				out = new BufferedWriter(new FileWriter("outfilename.txt"));
			} catch (IOException e) {
				IntentsregistryLog.logError(e);
			}


		}

		public void characters(char[] ch, int start, int length) {
			try {
				if (oiAction) {
					out.write(new String(ch, start, length));
					oiAction = false;
				}
				else if (title) {				
					out.write("=" + new String(ch, start,length) + "\n");
					title = false;
				}
			} catch (IOException e) {
				IntentsregistryLog.logError(e);
			}
		}
	}

	public void list( ) throws Exception {
		XMLReader parser = XMLReaderFactory.createXMLReader();
		parser.setContentHandler(new HowToHandler( ));
		parser.parse(new InputSource(new URL("http://www.openintents.org/en/xml/registry").openStream()));
	}

}