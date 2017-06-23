delete a.* from tbd_wage_detail a,tbd_wage b where a.FWAGE_ID=b.FID and b.facc_id=':FACC_ID';
delete from tbd_wage where FACC_ID=':FACC_ID';
delete a.* from tbd_wage_formula a where a.FACC_ID=':FACC_ID';
delete a.* from tbd_wage_voucher a where a.FACC_ID=':FACC_ID';