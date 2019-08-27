package com.zaqacu.service.impl;

import com.zaqacu.dao.StaffMapper;
import com.zaqacu.entity.Staff;
import com.zaqacu.entity.StaffArchives;
import com.zaqacu.entity.StaffPact;
import com.zaqacu.entity.UserLogin;
import com.zaqacu.entitydto.StaffDto;
import com.zaqacu.service.*;
import com.zaqacu.util.SaltUtils;
import com.zaqacu.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private PositionService positionService;
    @Autowired
    private StaffStatusService staffStatusService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingResultService trainingResultService;
    @Autowired
    private StaffPactService staffPactService;
    @Autowired
    private StaffArchivesService staffArchivesService;
    @Autowired
    private LoginAndOutService loginAndOutService;
    @Autowired
    private PersonnelService personnelService;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public HashMap<String, Object> getAllStaff(Staff staff) {
        HashMap<String, Object> map = new HashMap<>();
        staff.setStart((staff.getPage() - 1) * staff.getRows());
        List<Staff> staffList = staffMapper.selectAllStaffBySql(staff);
        List<StaffDto> staffDtos = new ArrayList<>();
        for (int i = 0; i < staffList.size(); i++) {
            StaffDto staffDto = new StaffDto();
            staffDto.setStaffUid(staffList.get(i).getStaffUid());
            staffDto.setStaffName(staffList.get(i).getStaffName());
            staffDto.setStaffSex(staffList.get(i).getStaffSex());
            staffDto.setStaffBirth(staffList.get(i).getStaffBirth());
            staffDto.setStaffPhone(staffList.get(i).getStaffPhone());
            staffDto.setStaffEmail(staffList.get(i).getStaffEmail());
            staffDto.setStaffXueli(staffList.get(i).getStaffXueli());
            staffDto.setStaffInTime(staffList.get(i).getStaffInTime());
            staffDto.setStaffNote(staffList.get(i).getStaffNote());
            staffDto.setPosition(positionService.getOnePositionById(staffList.get(i).getStaffPosition()));
            staffDto.setStaffStatus(staffStatusService.getOneStaffStatusById(staffList.get(i).getStaffStatusId()));
            staffDto.setDepartment(departmentService.getOneDepartmentById(staffList.get(i).getStaffDepartmentId()));
            if (trainingService.getOneTrainingById(staffList.get(i).getStaffTrainingId()) != null) {
                staffDto.setTraining(trainingService.getOneTrainingById(staffList.get(i).getStaffTrainingId()));
                staffDto.setTrainingResult(trainingResultService.getOneTrainingResult(trainingService.getOneTrainingById(staffList.get(i).getStaffTrainingId()).getTrainingStaffResultId()).getTrainingResult());
            } else {
                staffDto.setTrainingResult("未培训");
            }
            staffDtos.add(staffDto);
        }
        int total = staffMapper.selectCountStaff();
        map.put("total", total);
        map.put("rows", staffDtos);
        return map;
    }

    @Override
    public Staff getOneStaffByUid(String StaffUid) {
        return staffMapper.selectByPrimaryKey(StaffUid);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean removeStaff(String staffIds, String positionNames) {
        String[] ids = staffIds.split(",");
        String[] pnms = positionNames.split(",");
        List<String> staffUidList = new ArrayList<>();
        List<String> pnmsList = new ArrayList<>();
        Collections.addAll(staffUidList, ids);
        Collections.addAll(pnmsList, pnms);
        boolean flag1 = true;
        if (trainingService.getByStaffUidList(staffUidList) != null) {
            flag1 = trainingService.removeTrainingBatch(staffUidList);
        }
        boolean flag2 = staffPactService.removeBatch(staffUidList);
        boolean flag3 = staffArchivesService.removeBatch(staffUidList);
        //考勤
        boolean flag4 = staffMapper.deleteBatch(staffUidList) == 0 ? false : true;
        boolean flag5 = positionService.editPositionNumByPositionName(pnmsList);
        boolean flag6 = loginAndOutService.deleteByStaffUidList(staffUidList);

        boolean flag7=personnelService.removeBatchByStaffUid(staffUidList);
//            System.out.println(flag1);
        System.out.println(flag2);
        System.out.println(flag3);
        System.out.println(flag4);
        System.out.println(flag5);
        System.out.println(flag6);
        return flag1 && flag2 && flag3 && flag4 && flag5 && flag6;

    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean addStaff(String limitName, String userName, String userPassword,
                            String staffName, String staffSex, String staffBirth, String staffPhone,
                            String staffEmail, String staffXueli, String staffPosition,
                            String staffInTime, String staffStatusId, String staffNote, String archivesName,
                            String archivesInfo, String archivesNote, String pactCreateTime, String pactEndTime,
                            String pactInfo, String pactNote) {
//        System.out.println(limitName);
//        System.out.println(userName);
        if (!loginAndOutService.getOneUserLoginByUserName(userName)) {
            return false;
        }
//        System.out.println(userPassword);
        String saltPassword = SaltUtils.getSaltPassword(userPassword, userName);
        String staffUid = UUIDUtils.getOneUUId();
        UserLogin userLogin = new UserLogin();
        userLogin.setLimitName(limitName);
        userLogin.setUserName(userName);
        userLogin.setUserPassword(saltPassword);
        userLogin.setStaffUid(staffUid);
//        System.out.println(staffName);
//        System.out.println(staffSex);
//        System.out.println(staffBirth);
//        System.out.println(staffPhone);
//        System.out.println(staffEmail);
//        System.out.println(staffXueli);
//        System.out.println(staffDepartmentId);
//        System.out.println(staffPosition);
//        System.out.println(staffInTime);
//        System.out.println(staffStatusId);
//        System.out.println(staffNote);
        Staff staff = new Staff();
        staff.setStaffUid(staffUid);
        staff.setStaffName(staffName);
        staff.setStaffSex(staffSex);
        staff.setStaffBirth(staffBirth);
        staff.setStaffPhone(staffPhone);
        staff.setStaffEmail(staffEmail);
        staff.setStaffXueli(staffXueli);
        staff.setStaffPosition(Integer.parseInt(staffPosition.trim()));
        staff.setStaffDepartmentId(positionService.getOnePositionById(Integer.parseInt(staffPosition.trim())).getPositionDepartmentId());
        staff.setStaffInTime(staffInTime);
        staff.setStaffStatusId(Integer.parseInt(staffStatusId.trim()));
        staff.setStaffNote(staffNote);
//        System.out.println(archivesName);
//        System.out.println(archivesInfo);
//        System.out.println(archivesNote);
        StaffArchives staffArchives = new StaffArchives();
        staffArchives.setArchivesName(archivesName);
        staffArchives.setArchivesInfo(archivesInfo);
        staffArchives.setArchivesNote(archivesNote);
        staffArchives.setArcivesStaffUid(staffUid);
//        System.out.println(pactCreateTime);
//        System.out.println(pactEndTime);
//        System.out.println(pactInfo);
//        System.out.println(pactNote);
        StaffPact staffPact = new StaffPact();
        staffPact.setPactStaffUid(staffUid);
        staffPact.setPactCreateTime(pactCreateTime);
        staffPact.setPactEndTime(pactEndTime);
        staffPact.setPactInfo(pactInfo);
        staffPact.setPactNote(pactNote);
        staffPact.setPactStaffPositionId(Integer.parseInt(staffPosition.trim()));
//        System.out.println("aaaaa");
        boolean b1 = loginAndOutService.addUserLogin(userLogin);
//        System.out.println("bbbbb");
        boolean b2 = staffArchivesService.addStaffArchives(staffArchives);
//        System.out.println("ccccc");
        boolean b3 = staffPactService.addStaffPact(staffPact);
//        System.out.println("dddddd");
        boolean b4 = staffMapper.insertSelective(staff) != 1 ? false : true;
//        System.out.println("eeeee");
        boolean b5 = positionService.editPositionNumByPositionId(Integer.parseInt(staffPosition.trim()));
//        System.out.println("ffffff");
//        System.out.println(b1);
//        System.out.println(b2);
//        System.out.println(b3);
//        System.out.println(b4);
//        System.out.println(b5);
        return b1 && b2 && b3 && b4 && b5;
    }

    @Override
    public boolean editStaff(Staff staff) {
        System.out.println("fgdfasdfdf");
        return staffMapper.updateByPrimaryKeySelective(staff) != 1 ? false : true;
    }

    @Override
    public List<Staff> getAllStaffByPersonnel() {
        return staffMapper.selectAllStaffByPersonnel();
    }


}