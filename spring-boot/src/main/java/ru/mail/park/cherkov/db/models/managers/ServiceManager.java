package ru.mail.park.cherkov.db.models.managers;

import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.dao.ServiceDao;
import ru.mail.park.cherkov.db.models.api.Status;

@Service
public class ServiceManager {

    private ServiceDao serviceDao;

    public ServiceManager(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    public Status getStatus() {
        return serviceDao.getStatus();
    }

    public void cleear() {
        serviceDao.clear();
    }

}
