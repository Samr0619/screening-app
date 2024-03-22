package in.deloitte.screening.app.requisition.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.deloitte.screening.app.requisition.entities.Requisition_master;
import in.deloitte.screening.app.requisition.services.RequisitionService;


@RestController
@RequestMapping("/requisition")
public class RequisitionController {
	
	 private Logger logger = LogManager.getLogger(RequisitionController.class);
	
	@Autowired RequisitionService requisitionService;
	
	@GetMapping("/all-list")
	public ResponseEntity<List<Requisition_master>> getAllRequisitonList(){
		
		List<Requisition_master> list = requisitionService.getAllRequisitionList();
		
		return ResponseEntity.ok(list);
	}
}
