package in.deloitte.screening.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class ScreeningAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreeningAppApplication.class, args);
		
		
		
		String resume = "jyoti ranjan nandaa mobile 918147458618 emai nandajyoti2009@gmail.com   work experiences    i have 6 years of hands on experience on cisco ivr application development where i have worked on 6 ivr projects. my primary skill is cvp which is used for ivr application development. i have developed ivr applications for different customers based on the ivr scope shared from customer which includes database integration web service integrations and some basic menu options announcements prompt and collect data nodes  servlet node for writing ivr logic.  i have carried out system integration testing uat and go live activities also.  also i have 6 years of experiencein  development of products using technologies like core java spring framework micro services multithreading. i would like to be part of an organization that provides me with roles in the area of my expertise.     technical knowledge  • having good work experience in objectoriented programming oops. • having good work experience in multithreading.  • having good work experience in rest api spring boot mvc module.  • having good work experience in micro services. • cisco call studio 12.6 • cisco call studio 11.5 • cisco cvp 11.0 • hands on experience in ivr microsoft sql and oracle database. • java hibernate springservlets. • tomcat application server 6.x 7.x • excellent client interaction skills and proven experience in working independently as well as in a team.  professional summary   • working as senior software engineer with servion global solutions from nov 2017 to dec 2023.  future expectations   to seek a challenging opportunity where i can bring the best out of myself and expand my horizons on my strong java background and experience in the vast and evergrowing web applications.  software skills  • languages  java 1.8  • framework  spring hibernate.  • web server  apachetomcat8.0  • scripting languages  html  • java ide  eclipse intellij  • database tool  mysql  • building tool  maven  • operating system  windows10 ios     project experience   ➢ java developerivr developer – costco  project 1 costco ivr client  costco  position  senior software engineer  time period  nov2017 to dec 2023  project type  ivr application team size  8 tehnologies  java 1.8 spring boot rest api s3 git instance and deploying from dev to prod sql.  call studio 12.6   ➢ java developerivr developer – propharma   project 4  propharma ivr client  propharma position  software developer  time period  nov2017 to dec 2023 project type  ivr application team size  8 technologies  java 1.8 spring boot rest api s3 git instance and deploying from dev to prod sql.  call studio 12.6 roles and responsibilities   • interacting with customers for getting the ivr change requests and supporting the contact center 247 • working on the tickets raised by customer • managing svn for all the projects source code and documentations  education qualification  • 2013 btech in electronics and communications engineering from biju patnaik university of technology  i hereby declare that the above information provided by me is true to the best of my knowledge and belief.    thank you  jyoti ranjan nanda  ";
		Pattern pattern = Pattern.compile("\\b\\d+(\\.\\d+)? years?\\b", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(resume);

		while(matcher.find()) {
			String exp = matcher.group();
			System.out.println("exp : " + exp.split("y")[0]);
			break;
		}
	}

}
