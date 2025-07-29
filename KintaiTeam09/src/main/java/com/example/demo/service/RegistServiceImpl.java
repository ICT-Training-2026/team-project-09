package com.example.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Regist;
import com.example.demo.repository.RegistRepository;

import lombok.RequiredArgsConstructor;

// RegistServiceインタフェースを実装
@Service
@RequiredArgsConstructor
public class RegistServiceImpl implements RegistService {
	
	// RegistRepositoryインタフェースを実装しているBeanをDI
	private final RegistRepository registRepository;
	
	// 勤怠情報を登録するメソッド（インフラ層にデータを受け渡す）
	@Override
	public void add(Regist regist) {
		// registRepositoryImplのaddメソッドを呼び出し
		registRepository.add(regist);
	}
	
	// 今月の累積超過時間を取得するメソッド
	@Override
	public BigDecimal loadCumOverTime(String userId, int month) {
		BigDecimal overTime = registRepository.loadCumOverTime(userId, month);
		return overTime;
	}
	
	// 残り有給休暇日数を取得するメソッド
	@Override
	public BigDecimal loadNumPaidHoliday(String userId) {
		BigDecimal numPaidHoliday = registRepository.loadNumPaidHoliday(userId);
		return numPaidHoliday;
	}
	
	
	// 振出を取得するメソッド
	@Override
	public int loadNumHurikyu(String userId) {
		int numHurikyu = registRepository.loadNumHurikyu(userId);
		return numHurikyu;
	}
	
	// 振休を取得するメソッド
	@Override
	public int loadNumHurisyutsu(String userId) {
		int numHurisyutsu = registRepository.loadNumHurisyutsu(userId);
		return numHurisyutsu;
	}
	
	
	
}
