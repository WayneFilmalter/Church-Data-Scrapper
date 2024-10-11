/**
 * 
 */
/**
 * 
 */
module churchScrapper {
	requires java.desktop;
	requires org.jsoup;
	requires com.fasterxml.jackson.databind;

	opens churchScrapper.types to com.fasterxml.jackson.databind;

	requires com.fasterxml.jackson.annotation;

//	exports churchScrapper.apiTypes;
}