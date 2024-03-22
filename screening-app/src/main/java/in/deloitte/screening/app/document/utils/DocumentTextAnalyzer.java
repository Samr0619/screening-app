package in.deloitte.screening.app.document.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class DocumentTextAnalyzer {

	public String getStopWords(MultipartFile stopWordsFile) throws IOException {

		PDDocument stopWordsdoc = Loader.loadPDF(stopWordsFile.getBytes());
		PDFTextStripper pdfTextStripper = new PDFTextStripper();

		return pdfTextStripper.getText(stopWordsdoc).toLowerCase();
	}

	/**
	 * 
	 * 
	 * @param analyzer
	 * @param cleanedContent
	 * 
	 * @return
	 * 
	 * @throws IOException
	 * 
	 */
	public List<String> tokenizeText(Analyzer analyzer, String cv) throws IOException {

		TokenStream result = analyzer.tokenStream(null, cv.toLowerCase());
//		result = new PorterStemFilter(result);
		CharTermAttribute resultAttr = result.addAttribute(CharTermAttribute.class);
		List<String> tokens = new ArrayList<>();
		result.reset();

		while (result.incrementToken()) {
			tokens.add(resultAttr.toString());
		}
		result.close();

		return tokens;
	}
	
	/**
	 * 
	 * @return Returns stop words to be removed
	 * 
	 */
	public String stopWords() {
		return "0o 0s 3a 3b 3d 6b 6o a a1 a2 a3 a4 ab able about above abst ac accordance according accordingly across \r\n"
				+ "act actually ad added adj ae af affected affecting affects after afterwards ag again against ah ain ain't aj \r\n"
				+ "al all allow allows almost alone along already also although always am among amongst amoungst \r\n"
				+ "amount an and announce another any anybody anyhow anymore anyone anything anyway anyways \r\n"
				+ "anywhere ao ap apart apparently appear appreciate appropriate approximately ar are aren arent aren't \r\n"
				+ "arise around as a's aside ask asking associated at au auth av available aw away awfully ax ay az b b1 b2 \r\n"
				+ "b3 ba back bc bd be became because become becomes becoming been before beforehand begin \r\n"
				+ "beginning beginnings begins behind being believe below beside besides best better between beyond bi \r\n"
				+ "bill biol bj bk bl bn both bottom bp br brief briefly bs bt bu but bx by c c1 c2 c3 ca call came can cannot \r\n"
				+ "cant can't cause causes cc cd ce certain certainly cf cg ch changes ci cit cj cl clearly cm c'mon cn co com \r\n"
				+ "come comes con concerning consequently consider considering contain containing contains \r\n"
				+ "corresponding could couldn couldnt couldn't course cp cq cr cry cs c's ct cu currently cv cx cy cz d d2 da \r\n"
				+ "date dc dd de definitely describe described despite detail df di did didn didn't different dj dk dl do does \r\n"
				+ "doesn doesn't doing don done don't down downwards dp dr ds dt du due during dx dy e e2 e3 ea each \r\n"
				+ "ec ed edu ee ef effect eg ei eight eighty either ej el eleven else elsewhere em empty en end ending \r\n"
				+ "enough entirely eo ep eq er es especially est et et-al etc eu ev even ever every everybody everyone \r\n"
				+ "everything everywhere ex exactly example except ey f f2 fa far fc few ff fi fifteen fifth fify fill find fire first \r\n"
				+ "five fix fj fl fn fo followed following follows for former formerly forth forty found four fr from front fs ft \r\n"
				+ "fu full further furthermore fy g ga gave ge get gets getting gi give given gives giving gj gl go goes going \r\n"
				+ "gone got gotten gr greetings gs gy h h2 h3 had hadn hadn't happens hardly has hasn hasnt hasn't have \r\n"
				+ "haven haven't having he hed he'd he'll hello help hence her here hereafter hereby herein heres here's \r\n"
				+ "hereupon hers herself hes he's hh hi hid him himself his hither hj ho home hopefully how howbeit \r\n"
				+ "however how's hr hs http hu hundred hy i i2 i3 i4 i6 i7 i8 ia ib ibid ic id i'd ie if ig ignored ih ii ij il i'll im i'm \r\n"
				+ "immediate immediately importance important in inasmuch inc indeed index indicate indicated indicates \r\n"
				+ "information inner insofar instead interest into invention inward io ip iq ir is isn isn't it itd it'd it'll its it's \r\n"
				+ "itself iv i've ix iy iz j jj jr js jt ju just k ke keep keeps kept kg kj km know known knows ko l l2 la largely last \r\n"
				+ "lately later latter latterly lb lc le least les less lest let lets let's lf like liked likely line little lj ll ll ln lo look\r\n"
				+ "looking looks los lr ls lt ltd m m2 ma made mainly make makes many may maybe me mean means\r\n"
				+ "meantime meanwhile merely mg might mightn mightn't mill million mine miss ml mn mo more \r\n"
				+ "moreover most mostly move mr mrs ms mt mu much mug must mustn mustn't my myself n n2 na name \r\n"
				+ "namely nay nc nd ne near nearly necessarily necessary need needn needn't needs neither never \r\n"
				+ "nevertheless new next ng ni nine ninety nj nl nn no nobody non none nonetheless noone nor normally \r\n"
				+ "nos not noted nothing novel now nowhere nr ns nt ny o oa ob obtain obtained obviously oc od of off \r\n"
				+ "often og oh oi oj ok okay ol old om omitted on once one ones only onto oo op oq or ord os ot other \r\n"
				+ "others otherwise ou ought our ours ourselves out outside over overall ow owing own ox oz p p1 p2 p3 \r\n"
				+ "page pagecount pages par part particular particularly pas past pc pd pe per perhaps pf ph pi pj pk pl \r\n"
				+ "placed please plus pm pn po poorly possible possibly potentially pp pq pr predominantly present \r\n"
				+ "presumably previously primarily probably promptly proud provides ps pt pu put py q qj qu que quickly \r\n"
				+ "quite qv r r2 ra ran rather rc rd re readily really reasonably recent recently ref refs regarding regardless \r\n"
				+ "regards related relatively research research-articl respectively resulted resulting results rf rh ri right rj rl \r\n"
				+ "rm rn ro rq rr rs rt ru run rv ry s s2 sa said same saw say saying says sc sd se sec second secondly section \r\n"
				+ "see seeing seem seemed seeming seems seen self selves sensible sent serious seriously seven several sf \r\n"
				+ "shall shan shan't she shed she'd she'll shes she's should shouldn shouldn't should've show showed \r\n"
				+ "shown showns shows si side significant significantly similar similarly since sincere six sixty sj sl slightly sm \r\n"
				+ "sn so some somebody somehow someone somethan something sometime sometimes somewhat \r\n"
				+ "somewhere soon sorry sp specifically specified specify specifying sq sr ss st still stop strongly sub \r\n"
				+ "substantially successfully such sufficiently suggest sup sure sy system sz t t1 t2 t3 take taken taking tb tc \r\n"
				+ "td te tell ten tends tf th than thank thanks thanx that that'll thats that's that've the their theirs them \r\n"
				+ "themselves then thence there thereafter thereby thered therefore therein there'll thereof therere \r\n"
				+ "theres there's thereto thereupon there've these they theyd they'd they'll theyre they're they've thickv \r\n"
				+ "thin think third this thorough thoroughly those thou though thoughh thousand three throug through \r\n"
				+ "throughout thru thus ti til tip tj tl tm tn to together too took top toward towards tp tq tr tried tries truly \r\n"
				+ "try trying ts t's tt tv twelve twenty twice two tx u u201d ue ui uj uk um un under unfortunately unless \r\n"
				+ "unlike unlikely until unto uo up upon ups ur us use used useful usefully usefulness uses using usually ut v \r\n"
				+ "va value various vd ve ve very via viz vj vo vol vols volumtype vq vs vt vu w wa want wants was wasn \r\n"
				+ "wasnt wasn't way we wed we'd welcome well we'll well-b went were we're weren werent weren't we've \r\n"
				+ "what whatever what'll whats what's when whence whenever when's where whereafter whereas \r\n"
				+ "whereby wherein wheres where's whereupon wherever whether which while whim whither who whod \r\n"
				+ "whoever whole who'll whom whomever whos who's whose why why's wi widely will willing wish with \r\n"
				+ "within without wo won wonder wont won't words world would wouldn wouldnt wouldn't www x x1 x2 \r\n"
				+ "x3 xf xi xj xk xl xn xo xs xt xv xx y y2 yes yet yj yl you youd you'd you'll your youre you're yours yourself \r\n"
				+ "yourselves you've yr ys yt z zero zi zz and for in to a in an of ";
	}

}
