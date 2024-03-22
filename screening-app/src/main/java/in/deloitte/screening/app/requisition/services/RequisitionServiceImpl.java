package in.deloitte.screening.app.requisition.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.deloitte.screening.app.requisition.entities.Requisition_master;
import in.deloitte.screening.app.requisition.repositories.RequisitonRepository;

@Service
public class RequisitionServiceImpl implements RequisitionService{
	
	@Autowired RequisitonRepository requisitonRepository;

	@Override
	public List<Requisition_master> getAllRequisitionList() {
		// TODO Auto-generated method stub
		return requisitonRepository.findAll();
	}

}
