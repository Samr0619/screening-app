package in.deloitte.screening.app.requisition.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import in.deloitte.screening.app.requisition.entities.Requisition_master;

public interface RequisitonRepository extends JpaRepository<Requisition_master, Integer>{

}
