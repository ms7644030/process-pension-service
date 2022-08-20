package com.processPension.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.processPension.customException.BusinessException;
import com.processPension.entities.Bankdetail;
import com.processPension.entities.PensionDetail;
import com.processPension.entities.PensionerDetail;

@Service
public class PensionDetailServiceImpl implements ProcessPensionService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private PensionDetail pensionDetail;

	@Value("${PENSIONER_DETAIL_URI:http://localhost:9001}")
	private String pensionerDetailHost;

	@Override
	public PensionDetail getPensionDetailByAadhar(Long aadhaar) {

		/*
		 * PensionerDetail pensionerDetail=
		 * restTemplate.exchange("http://PENSIONER-DETAIL-SERVICE/pensionerDetail/"+
		 * aadhaar, HttpMethod.GET,null, PensionerDetail.class).getBody();
		 */
		// ResponseEntity<PensionerDetail> response =
		// restTemplate.getForEntity("http://PENSIONER-DETAIL-SERVICE/pensionerDetail/"+aadhaar,PensionerDetail.class);
		// ResponseEntity<Object> response =
		// restTemplate.getForEntity("http://PENSIONER-DETAIL-SERVICE/pensionerDetail/"+aadhaar,Object.class);

		/*
		 * if(response.getBody() instanceof PensionerDetail) {
		 * 
		 * 
		 * PensionerDetail pensionerDetail = (PensionerDetail) response.getBody();
		 * 
		 * String type = pensionerDetail.getPensioner().getSelfOrFamily().trim();
		 * 
		 * 
		 * double lastEarnedSalary = pensionerDetail.getPensioner().getSalaryEarned();
		 * double allowances = pensionerDetail.getPensioner().getAllowances();
		 * 
		 * Bankdetail bankdetail = pensionerDetail.getBankdetail(); String bankType =
		 * bankdetail.getPublicOrprivate_bank().trim();
		 * 
		 * if (type.equalsIgnoreCase("self")) {
		 * 
		 * double pensionAmmount = (0.80*lastEarnedSalary)+allowances;
		 * 
		 * pensionDetail.setPensionAmount(pensionAmmount);
		 * 
		 * } else if (type.equalsIgnoreCase("family")) {
		 * 
		 * double pensionAmmount = (0.50*lastEarnedSalary)+allowances;
		 * 
		 * pensionDetail.setPensionAmount(pensionAmmount);
		 * 
		 * }
		 * 
		 * if (bankType.equalsIgnoreCase("public")) {
		 * 
		 * pensionDetail.setBankServiceCharge(500.0);
		 * 
		 * }
		 * 
		 * else if (bankType.equalsIgnoreCase("private")) {
		 * 
		 * pensionDetail.setBankServiceCharge(550.0);
		 * 
		 * }
		 * 
		 * 
		 * } else if(response.getBody() instanceof String) {
		 * 
		 * String value = (String) response.getBody();
		 * 
		 * if (value.equalsIgnoreCase("Invalid"))
		 * 
		 * throw new BusinessException("601","Invalid Aadhaar");
		 * 
		 * }
		 * 
		 * 
		 * return pensionDetail;
		 * 
		 */

		// ResponseEntity<String> response = restTemplate
		// .getForEntity("http://PENSIONER-DETAIL-SERVICE/pensionerDetail/" + aadhaar,
		// String.class);

		ResponseEntity<String> response = restTemplate.getForEntity(
				pensionerDetailHost + "/api/pensioner-detail-service/pensionerDetail/" + aadhaar, String.class);

		String value = (String) response.getBody();

		if (value.equalsIgnoreCase("Invalid")) {

			throw new BusinessException("601", "Invalid Aadhaar");
		} else {

			ObjectMapper mapper = new ObjectMapper();

			try {
				PensionerDetail pensionerDetail = mapper.readValue(value, PensionerDetail.class);

				// PensionerDetail pensionerDetail = (PensionerDetail) response.getBody();

				String type = pensionerDetail.getPensioner().getSelfOrFamily().trim();

				double lastEarnedSalary = pensionerDetail.getPensioner().getSalaryEarned();
				double allowances = pensionerDetail.getPensioner().getAllowances();

				Bankdetail bankdetail = pensionerDetail.getBankdetail();
				String bankType = bankdetail.getPublicOrprivate_bank().trim();

				if (type.equalsIgnoreCase("self")) {

					double pensionAmmount = (0.80 * lastEarnedSalary) + allowances;

					pensionDetail.setPensionAmount(pensionAmmount);

				} else if (type.equalsIgnoreCase("family")) {

					double pensionAmmount = (0.50 * lastEarnedSalary) + allowances;

					pensionDetail.setPensionAmount(pensionAmmount);

				}

				if (bankType.equalsIgnoreCase("public")) {

					pensionDetail.setBankServiceCharge(500.0);

				}

				else if (bankType.equalsIgnoreCase("private")) {

					pensionDetail.setBankServiceCharge(550.0);

				}

			} catch (JsonProcessingException e) {

				e.printStackTrace();
			}

			return pensionDetail;
		}
	}

}
