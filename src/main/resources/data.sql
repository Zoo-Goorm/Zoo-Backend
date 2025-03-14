INSERT IGNORE NTO keyword (id, name)
VALUES
    (1, '핵심 기술'),
    (2, '심화 기술'),
    (3, '산업 활용 사례'),
    (4, '글로벌'),
    (5, '비즈니스'),
    (6, '미래 전망');

INSERT IGNORE INTO event (
    id, name, description, location, start_time, end_time
) VALUES
(1, 'Right AIgent: Smart Agent, Smarter Future',
 'AI 에이전트 관련 기술 및 활용 사례를 공유하는 온/오프라인 하이브리드 컨퍼런스입니다. IT 업계 종사자, 학생, 취업 준비생을 대상으로 진행됩니다. 참가비는 오프라인 79,000원, 온라인 39,000원입니다.',
 '서울 코엑스',
 '2025-04-03 10:00:00', '2025-04-04 17:30:00');


-- 연사 데이터 (Speaker)
INSERT IGNORE INTO speaker (id, name, email, phone_number) VALUES
(1, '페드로 도밍고스', 'pedro@example.com', '010-1111-1111'),
(2, '데이비드 실버', 'david@example.com', '010-2222-2222'),
(3, '야닉 킬처', 'yannic@example.com', '010-3333-3333'),
(4, '레슬리 윌러비', 'leslie@example.com', '010-4444-4444'),
(5, '크리스토퍼 매닝', 'christopher@example.com', '010-5555-5555'),
(6, '해리슨 체이스', 'harrison@example.com', '010-6666-6666'),
(7, '카이푸 리', 'kaifu.lee@example.com', '010-7777-7777'),
(8, '이경전', 'kyungjeon.lee@example.com', '010-8888-8888'),
(9, '장병탁', 'byungtak.jang@example.com', '010-9999-9999'),
(10, '최재식', 'jaesik.choi@example.com', '010-1010-1010'),
(11, '이두희', 'doohee.lee@example.com', '010-1111-1111'),
(12, '송길영', 'kilyoung.song@example.com', '010-1212-1212'),
(13, '김이언', 'ieon.kim@example.com', '010-1313-1313'),
(14, '김재호', 'jaeho.kim@example.com', '010-1414-1414'),
(15, '윤성로', 'seongro.yoon@example.com', '010-1515-1515');

-- 세션 데이터 (Session)
INSERT IGNORE INTO session (
    id, event_id, speaker_id, event_day, name, short_description, long_description,
    max_capacity, participant_count, start_time, end_time, status, video_link, location
) VALUES
(
    1, 1, 1, 1,
    'AI 에이전트: 현재를 넘어서 미래로',
    'AI 에이전트의 개념과 발전 과정을 정리하고, 최신 연구 동향과 기술 트렌드를 살펴본다.',
    'AI 에이전트는 단순한 자동화에서 벗어나 인간과 협력하는 지능형 시스템으로 진화하고 있다. 이 강연에서는 AI 에이전트의 핵심 개념을 정리하고, 최신 연구 및 기술 트렌드를 분석한다. 또한, 자연어 처리, 강화 학습, 멀티모달 AI 등의 발전이 에이전트 기술에 어떤 변화를 가져오는지 살펴본다. 이를 바탕으로 향후 5~10년간 AI 에이전트가 비즈니스, 산업, 사회에 미칠 영향을 예측한다. AI가 단순한 도구를 넘어 능동적 협력자로 자리 잡는 미래를 함께 전망한다.',
    1000, 0,
    '2025-04-03 10:00:00', '2025-04-03 12:00:00',
    'BEFORE_START',
    'https://example.com/ai-agent-video',
    '3F 오디토리움'
),
(
    2, 1, 2, 1,
    '강화학습: AI가 스스로 배우는 법',
    '강화학습(RL)의 핵심 개념과 원리를 정리하고, 최신 연구 및 실제 적용 사례를 소개한다.',
    '강화학습(RL)은 보상을 통해 AI가 스스로 학습하는 강력한 기술로, 다양한 산업에서 혁신을 주도하고 있다. 이 강연에서는 RL의 기본 개념과 주요 알고리즘(예: Q-learning, DDPG, PPO)을 설명한다. 또한, 알파고(AlphaGo), 자율주행 차량, 로보틱스, 금융 트레이딩 시스템 등 RL이 실제로 활용된 대표 사례를 분석한다. 최신 연구 동향과 RL의 한계점 및 해결 방안도 함께 다룬다.',
    300, 0,
    '2025-04-03 10:00:00', '2025-04-03 12:00:00',
    'BEFORE_START',
    'https://example.com/rl-video',
    '201호'
),
(
    3, 1, 3, 1,
    '대형 언어 모델(LLM): 인공지능의 새로운 지평',
    '대형 언어 모델(LLM)의 개념과 기술 원리를 설명하고, 최신 연구 및 산업 활용 사례를 소개한다.',
    '대형 언어 모델(LLM)은 AI의 자연어 이해 및 생성 능력을 획기적으로 향상시키며, 다양한 산업에서 혁신을 주도하고 있다. 이 강연에서는 LLM의 기본 개념과 핵심 기술(Transformer 아키텍처, 사전 학습 및 미세 조정 기법 등)을 정리한다.',
    300, 0,
    '2025-04-03 10:00:00', '2025-04-03 12:00:00',
    'BEFORE_START',
    'https://example.com/llm-video',
    '202호'
),
(
    4, 1, 4, 1,
    'AI 기반 자동화: 혁신을 가속하는 힘',
    '자동화 기술의 발전 과정과 핵심 개념을 정리하고, AI 기반 자동화의 최신 연구 및 실제 적용 사례를 소개한다.',
    '자동화 기술은 단순 반복 작업을 넘어서, 인공지능(AI)과 결합하며 더 지능적이고 효율적인 시스템으로 발전하고 있다. 이 강연에서는 자동화의 기본 개념과 AI 기반 자동화의 핵심 기술을 소개하고, 제조, 물류, 금융 등 다양한 사례를 분석한다.',
    300, 0,
    '2025-04-03 10:00:00', '2025-04-03 12:00:00',
    'BEFORE_START',
    'https://example.com/automation-video',
    '203호'
),
(
    5, 1, 5, 1,
    '자연어 처리(NLP): AI가 언어를 이해하는 법',
    '자연어 처리(NLP)는 AI가 인간의 언어를 이해하고 생성하는 데 필수적인 기술이다.',
    '자연어 처리(NLP)는 AI가 인간의 언어를 이해하고 생성하는 데 필수적인 기술이다. 이 강연에서는 NLP의 핵심 기술과 최신 연구 동향을 정리하고, AI 챗봇, 기계 번역, 감성 분석 등 실제 사례를 살펴본다.',
    160, 0,
    '2025-04-03 10:00:00', '2025-04-03 12:00:00',
    'BEFORE_START',
    'https://example.com/nlp-video',
    '205호'
),
(
    6, 1, 6, 1,
    'AI 에이전트 설계와 오픈소스 개발 도구: 직접 만들어보는 지능형 시스템',
    'AI 에이전트를 설계하는 핵심 원리와 주요 아키텍처를 소개하고, 오픈소스 프레임워크 및 개발 도구의 활용법을 설명한다.',
    'AI 에이전트는 사용자의 요구를 이해하고, 데이터를 처리하며, 자율적으로 의사결정을 내리는 시스템이다. 이 강연에서는 AI 에이전트 모델의 핵심 설계 원리와 오픈소스 프레임워크 및 개발 도구를 소개하고, 실제 사용법을 시연한다.',
    160, 0,
    '2025-04-03 10:00:00', '2025-04-03 12:00:00',
    'BEFORE_START',
    'https://example.com/agent-video',
    '209호'
),
-- 13:00 ~ 14:30 세션
(7, 1, 7, 2,
'AI 에이전트 혁명: 산업을 재편하는 지능형 시스템',
'AI 에이전트가 다양한 산업을 어떻게 혁신하고 있는지 글로벌 선도 기업들의 사례를 통해 분석한다.',
'AI 에이전트는 금융, 헬스케어, 제조, 고객 서비스, 이커머스 등 다양한 산업에서 자동화와 최적화를 통해 혁신을 주도하고 있다. 이 강연에서는 글로벌 선도 기업(Google, Amazon, Tesla, JP Morgan, Alibaba 등)의 AI 에이전트 도입 사례를 분석하고, 성공과 실패 요인을 비교한다.',
1000, 0, '2025-04-03 13:00:00', '2025-04-03 14:30:00', 'BEFORE_START',
'https://example.com/ai-revolution', '3F 오디토리움'),

-- 14:30 ~ 15:30 세션
(8, 1, 8, 2,
'비즈니스 혁신을 위한 AI 에이전트 활용 전략',
'AI 에이전트를 활용해 기업이 경쟁력을 강화하는 전략과 성공적인 도입 사례를 분석한다.',
'AI 에이전트는 기업의 업무 효율을 극대화하고, 고객 경험을 혁신하며, 데이터 기반 의사결정을 지원하는 핵심 기술로 자리 잡고 있다.',
300, 0, '2025-04-03 14:30:00', '2025-04-03 15:30:00', 'BEFORE_START',
'https://example.com/ai-business-strategy', '201호'),

(9, 1, 9, 2,
'에이전틱 AI로 업무 효율을 극대화하다',
'AI 에이전트를 활용해 업무 생산성을 극대화한 사례를 분석하고, 실무 적용 방안을 제시한다.',
'에이전틱 AI는 업무 자동화, 데이터 분석, AI 비서, 협업 툴 등 다양한 분야에서 활용되며 업무 효율성을 극대화한다.',
300, 0, '2025-04-03 14:30:00', '2025-04-03 15:30:00', 'BEFORE_START',
'https://example.com/ai-efficiency', '202호'),

(10, 1, 10, 2,
'AI 기반 의사결정 시스템: 가능성과 한계를 넘어서',
'AI 기반 의사결정 시스템의 장점과 한계를 분석하고, 실용적인 도입 전략을 소개한다.',
'AI 기반 의사결정 시스템은 데이터 기반으로 의사결정을 자동화하지만, 편향성이나 설명 가능성 등 다양한 도전 과제를 가진다.',
300, 0, '2025-04-03 14:30:00', '2025-04-03 15:30:00', 'BEFORE_START',
'https://example.com/ai-decision', '203호'),

-- 15:30 ~ 16:30 세션
(11, 1, 11, 2,
'AI 에이전트, 소프트웨어 개발을 혁신하다',
'AI 에이전트가 소프트웨어 개발에서 어떻게 활용되고 있는지 소개한다.',
'AI 코딩 도구의 활용 사례, 코드 자동 생성, 자동 디버깅, 테스트 자동화 등 AI 에이전트의 개발 활용 사례를 다룬다.',
300, 0, '2025-04-03 15:30:00', '2025-04-03 16:30:00', 'BEFORE_START',
'https://example.com/ai-dev', '201호'),

(12, 1, 12, 2,
'AI 에이전트, 이커머스를 재편하다',
'AI 에이전트가 이커머스에서 어떻게 활용되는지 사례를 통해 소개한다.',
'AI 기반 추천 시스템, 자동화된 고객 서비스, 수요 예측 등의 사례를 통해 AI가 이커머스를 어떻게 혁신하고 있는지 살펴본다.',
300, 0, '2025-04-03 15:30:00', '2025-04-03 16:30:00', 'BEFORE_START',
'https://example.com/ai-ecommerce', '202호'),

(13, 1, 13, 2,
'AI 에이전트, 의료를 진단하고 치료하다',
'AI 에이전트가 의료 분야에서 어떻게 사용되는지 소개하고, 미래 발전 방향을 제시한다.',
'AI를 활용한 진단 보조, 신약 개발, 의료 데이터 분석 등의 최신 사례를 살펴보고, 의료 AI의 윤리적 문제와 한계를 논의한다.',
300, 0, '2025-04-03 15:30:00', '2025-04-03 16:30:00', 'BEFORE_START',
'https://example.com/ai-healthcare', '203호'),

(14, 1, 14, 2,
'AI 에이전트, 도로 위에서 스스로 판단하다',
'자율주행 기술에서 AI 에이전트의 역할과 도전 과제를 소개한다.',
'자율주행 AI의 핵심 기술과 글로벌 기업들의 도입 사례, 법적·윤리적 이슈를 다룬다.',
160, 0, '2025-04-03 15:30:00', '2025-04-03 16:30:00', 'BEFORE_START',
'https://example.com/ai-autonomous', '205호'),

(15, 1, 15, 2,
'AI 에이전트, 제조업을 자동화하고 최적화하다',
'AI 에이전트가 제조업에서 어떻게 활용되고 있는지 소개하고, 효율성 향상 방안을 제시한다.',
'스마트 팩토리, 예측 유지보수, 생산 최적화 등 AI 기반 제조업 혁신 사례를 소개한다.',
160, 0, '2025-04-03 15:30:00', '2025-04-03 16:30:00', 'BEFORE_START',
'https://example.com/ai-manufacturing', '209호');


--day 2
-- 연사 데이터 (Speaker)
INSERT INTO speaker (id, name, email, phone_number) VALUES
(16, '김연희', 'yeonhee.kim@kaist.ac.kr', '010-7777-7777'),
(17, '이경미', 'kyungmi.lee@example.com', '010-8888-8888'),
(18, '박성수', 'sungsoo.park@example.com', '010-9999-9999'),
(19, '김대영', 'daeyoung.kim@example.com', '010-1010-1010'),
(20, '이상훈', 'sanghoon.lee@example.com', '010-1111-1111'),
(21, '한지영', 'jiyoung.han@example.com', '010-1212-1212'),
(22, '김동신', 'dongshin.kim@example.com', '010-1313-1313'),
(23, '마르쿠스 브루너마이어', 'markus.b@example.com', '010-1414-1414'),
(24, '라파엘 뎀러', 'rafael.d@example.com', '010-1515-1515'),
(25, '박진호', 'jinho.park@example.com', '010-1616-1616'),
(26, '이영주', 'youngjoo.lee@example.com', '010-1717-1717'),
(27, '임채승', 'chaeseung.lim@example.com', '010-1818-1818'),
(28, '김명주', 'myungjoo.kim@example.com', '010-1919-1919');




-- 세션 데이터 (Session)
INSERT IGNORE INTO session (
    id, event_id, speaker_id, event_day, name, short_description, long_description,
    max_capacity, participant_count, start_time, end_time, status, video_link, location
) VALUES
(16, 1, 16, 2, '강화학습과 계획: AI의 자율적 의사결정 방식',
 '강화학습과 계획(Planning) 기반 AI가 어떻게 자율적 의사결정을 내리는지 설명하고, 최신 연구 및 적용 사례를 분석한다.',
 '강화학습(RL)과 계획(Planning)은 AI가 환경을 탐색하고 최적의 행동을 결정하는 데 필수적인 요소다.',
 300, 0, '2025-04-04 10:00:00', '2025-04-04 11:00:00', 'BEFORE_START',
 'https://example.com/rl-planning', '201호'),

(17, 1, 17, 2, 'AI의 학습 속도를 높이는 방법: Meta-Learning과 Transfer Learning',
 'Meta-Learning과 Transfer Learning 기술을 활용하여 AI의 학습 속도를 가속화하고 적은 데이터로도 효율적으로 학습하는 방법을 소개한다.',
 'AI가 새로운 환경에서 빠르게 적응하려면 기존의 학습 경험을 활용해야 한다.',
 300, 0, '2025-04-04 10:00:00', '2025-04-04 11:00:00', 'BEFORE_START',
 'https://example.com/meta-learning', '202호'),

(18, 1, 18, 2, 'Edge AI와 이벤트 기반 아키텍처: 실시간 AI의 미래',
 'Edge AI와 Event-Driven Architecture를 활용하여 실시간 대응이 가능한 AI 시스템을 구축하는 방법을 소개한다.',
 'AI 시스템이 실시간으로 의사결정을 내리려면 중앙 서버가 아닌 엣지(Edge)에서 동작해야 한다.',
 300, 0, '2025-04-04 10:00:00', '2025-04-04 11:00:00', 'BEFORE_START',
 'https://example.com/edge-ai', '203호'),

(19, 1, 19, 2, 'AI 에이전트가 협력하는 방법: 자율적 의사결정과 분산 학습',
 '다중 AI 에이전트 시스템에서 자율적 의사결정이 이루어지는 원리를 설명하고, 분산 학습을 통한 협업 방식과 최신 연구 사례를 분석한다.',
 '다중 AI 에이전트(Multi-Agent AI)는 서로 협력하고 경쟁하며 복잡한 문제를 해결하는 능력을 갖춘 시스템이다.',
 300, 0, '2025-04-04 11:00:00', '2025-04-04 12:00:00', 'BEFORE_START',
 'https://example.com/ai-cooperation', '201호'),

(20, 1, 20, 2, 'AI 에이전트 간 소통의 기술: 멀티 에이전트 시스템의 정보 공유 전략',
 '멀티 에이전트 시스템에서 AI가 효과적으로 정보를 공유하고 협력하는 방법을 설명한다.',
 '다중 AI 에이전트가 협력하기 위해서는 효과적인 통신과 정보 공유 전략이 필수적이다.',
 300, 0, '2025-04-04 11:00:00', '2025-04-04 12:00:00', 'BEFORE_START',
 'https://example.com/agent-communication', '202호'),

(21, 1, 21, 2, '대규모 AI 에이전트 시뮬레이션: 복잡계를 이해하는 새로운 방법',
 '대규모 멀티 에이전트 시스템을 시뮬레이션하여 복잡한 사회·경제·기술 시스템을 분석하는 최신 연구를 소개한다.',
 '대규모 멀티 에이전트 시스템 시뮬레이션은 인간 사회, 경제, 환경 시스템의 복잡한 상호작용을 모델링하는 데 중요한 도구가 되고 있다.',
 300, 0, '2025-04-04 11:00:00', '2025-04-04 12:00:00', 'BEFORE_START',
 'https://example.com/ai-simulation', '203호'),

(22, 1, 22, 2, 'AI 에이전트 시장의 기회와 도전: 스타트업과 투자 전략',
 'AI 에이전트 시장의 성장 가능성과 스타트업 생태계를 분석하고, 기업들이 AI 기술을 사업화하는 전략을 소개한다.',
 'AI 에이전트 기술은 기업과 투자자들에게 새로운 기회를 제공한다.',
 1000, 0, '2025-04-04 13:00:00', '2025-04-04 14:30:00', 'BEFORE_START',
 'https://example.com/ai-market', '3F 오디토리움'),

(23, 1, 23, 2, 'AI 에이전트, 금융 시장을 혁신하다',
 'AI 에이전트가 금융 시장을 어떻게 혁신하고 있는지 분석합니다.',
 'AI 기반 알고리즘 트레이딩, 리스크 평가, 자동화된 금융 컨설팅 등을 중심으로 금융 AI의 미래를 논의합니다.',
 300, 0, '2025-04-04 14:30:00', '2025-04-04 15:30:00', 'BEFORE_START',
 'https://example.com/ai-finance', '201호'),

(24, 1, 24, 2, 'AI 에이전트, 글로벌 물류를 최적화하다',
 'AI 에이전트가 물류 및 공급망에서 어떻게 효율성을 높이고 있는지 소개합니다.',
 'AI 기반 실시간 재고 관리와 최적 경로 설정 등의 사례를 소개합니다.',
 300, 0, '2025-04-04 14:30:00', '2025-04-04 15:30:00', 'BEFORE_START',
 'https://example.com/ai-logistics', '202호'),

(25, 1, 25, 2, '스마트 에너지 시대: AI 에이전트의 역할',
 'AI 에이전트가 에너지 효율성을 높이고 지속 가능성을 실현하는 방법을 소개합니다.',
 '스마트 그리드와 에너지 최적화 기술을 중심으로 AI의 역할을 설명합니다.',
 300, 0, '2025-04-04 14:30:00', '2025-04-04 15:30:00', 'BEFORE_START',
 'https://example.com/ai-energy', '203호'),

(26, 1, 26, 2, 'AI 에이전트, 맞춤형 교육을 실현하다',
 'AI 에이전트가 교육 분야에서 개인 맞춤형 학습, 지능형 튜터링 시스템, 자동 평가 시스템 등을 어떻게 실현하는지 분석한다. AI 교육 기술의 미래를 전망한다.',
 'AI는 학생별 맞춤 학습을 가능하게 하며, 교육의 접근성을 높이는 역할을 하고 있다. 이 강연에서는 AI 기반 개인화 학습 추천 시스템, 지능형 튜터링 시스템(ITS), 자동 과제 평가 AI, 학습 분석(Learning Analytics) 기술 등을 소개한다. 또한 AI가 교육 격차를 해소하는 데 기여할 가능성과 윤리적 문제(데이터 프라이버시, 편향 문제)를 논의하며, 미래 교육에서 AI의 역할을 전망한다.',
 160, 0, '2025-04-04 14:30:00', '2025-04-04 15:30:00', 'BEFORE_START',
 'https://example.com/ai-education', '205호'),

(27, 1, 27, 2, 'AI 에이전트, 고객 경험을 혁신하다',
 'AI 에이전트가 고객 서비스에서 챗봇, 음성 비서, 감성 분석 등의 기술을 통해 고객 경험을 어떻게 향상시키는지 분석한다. AI 기반 고객 응대의 미래를 전망한다.',
 'AI 에이전트는 고객 서비스의 효율성과 정확성을 높이며, 인간 상담원과 협력하는 방식으로 진화하고 있다. 이 강연에서는 AI 챗봇, 음성 인식 기반 고객 지원, 감성 분석 AI, 실시간 상담 자동화 등의 사례를 분석한다. 또한 AI 도입이 고객 만족도에 미치는 영향을 살펴보고, AI 상담 시스템이 가지는 한계와 윤리적 문제를 논의한다.',
 160, 0, '2025-04-04 14:30:00', '2025-04-04 15:30:00', 'BEFORE_START',
 'https://example.com/ai-customer-experience', '209호'),

(28, 1, 28, 2, 'AI 에이전트와 미래 사회: 윤리, 정책/법, 지속가능성',
 'AI 에이전트의 윤리적 문제, 프라이버시 보호, 법적 규제, 지속가능성 등의 주요 이슈를 논의하는 컨퍼런스 마무리 세션이다.',
 'AI 에이전트가 점점 더 자율성을 갖추면서 보안, 프라이버시, 윤리적 문제에 대한 논의가 필수적이 되고 있다. 이 세션에서는 AI 에이전트의 보안 및 데이터 보호, 윤리적 설계 원칙, 법적·정책적 고려사항을 종합적으로 다룬다. 또한 기업과 정부가 AI 거버넌스를 어떻게 구축할 것인지, 지속가능성과 환경 모니터링을 위한 AI의 역할은 무엇인지 논의하며, 기술적 과제와 기회를 살펴본다. 마지막으로 패널 토론과 실시간 질의응답을 통해 참가자들과 함께 AI 에이전트의 책임 있는 사용과 미래 방향성을 고민하는 시간을 가진다.',
 1000, 0, '2025-04-04 15:30:00', '2025-04-04 16:30:00', 'BEFORE_START',
 'https://example.com/ai-ethics', '3F 오디토리움');



INSERT IGNORE INTO session_keyword (session_id, keyword_id) VALUES
-- 세션 1: AI 에이전트: 현재를 넘어서 미래로 (글로벌, 미래 전망)
(1, 4),
(1, 6),

-- 세션 2: 강화학습: AI가 스스로 배우는 법 (핵심 기술, 글로벌, 미래 전망)
(2, 1),
(2, 4),
(2, 6),

-- 세션 3: 대형 언어 모델(LLM): 인공지능의 새로운 지평 (핵심 기술, 글로벌, 미래 전망)
(3, 1),
(3, 4),
(3, 6),

-- 세션 4: AI 기반 자동화: 혁신을 가속하는 힘 (핵심 기술, 글로벌, 미래 전망)
(4, 1),
(4, 4),
(4, 6),

-- 세션 5: 자연어 처리(NLP): AI가 언어를 이해하는 법 (핵심 기술, 글로벌, 미래 전망)
(5, 1),
(5, 4),
(5, 6),

-- 세션 6: AI 에이전트 설계와 오픈소스 개발 도구 (핵심 기술, 글로벌, 미래 전망)
(6, 1),
(6, 4),
(6, 6),

(7, 4),
(7, 3),
(7, 5),

-- 세션 8: 비즈니스 혁신을 위한 AI 에이전트 활용 전략 (산업 활용 사례, 비즈니스)
(8, 3),
(8, 5),

-- 세션 9: 에이전틱 AI로 업무 효율을 극대화하다 (산업 활용 사례, 비즈니스)
(9, 3),
(9, 5),

-- 세션 10: AI 기반 의사결정 시스템: 가능성과 한계를 넘어서 (산업 활용 사례, 비즈니스)
(10, 3),
(10, 5),

-- 15:30 ~ 16:30 - 산업별 AI 에이전트 활용 1
-- 세션 11: AI 에이전트, 소프트웨어 개발을 혁신하다 (산업 활용 사례, 미래 전망)
(11, 3),
(11, 6),

-- 세션 12: AI 에이전트, 이커머스를 재편하다 (산업 활용 사례, 미래 전망)
(12, 3),
(12, 6),

-- 세션 13: AI 에이전트, 의료를 진단하고 치료하다 (산업 활용 사례, 미래 전망)
(13, 3),
(13, 6),

-- 세션 14: AI 에이전트, 도로 위에서 스스로 판단하다 (산업 활용 사례, 미래 전망)
(14, 3),
(14, 6),

-- 세션 15: AI 에이전트, 제조업을 자동화하고 최적화하다 (산업 활용 사례, 미래 전망)
(15, 3),
(15, 6),


-- 세션 16: 강화학습과 계획: AI의 자율적 의사결정 방식 (심화 기술)
(16, 2),

-- 세션 17: AI의 학습 속도를 높이는 방법: Meta-Learning과 Transfer Learning (심화 기술)
(17, 2),

-- 세션 18: Edge AI와 이벤트 기반 아키텍처: 실시간 AI의 미래 (심화 기술)
(18, 2),

-- SessionKeyword 데이터 삽입 (11:00 ~ 12:00 - 멀티 AI 에이전트 시스템)
-- 세션 19: AI 에이전트가 협력하는 방법: 자율적 의사결정과 분산 학습 (심화 기술)
(19, 2),

-- 세션 20: AI 에이전트 간 소통의 기술: 멀티 에이전트 시스템의 정보 공유 전략 (심화 기술)
(20, 2),

-- 세션 21: 대규모 AI 에이전트 시뮬레이션: 복잡계를 이해하는 새로운 방법 (심화 기술)
(21, 2),

-- 세션 22: AI 에이전트 시장의 기회와 도전: 스타트업과 투자 전략 (산업 활용 사례, 글로벌, 비즈니스, 미래 전망)
(22, 3),
(22, 4),
(22, 5),
(22, 6),

-- SessionKeyword 데이터 삽입 (14:30 ~ 15:30 - 산업별 AI 에이전트 활용 2)
-- 세션 23: AI 에이전트, 금융 시장을 혁신하다 (산업 활용 사례, 글로벌, 미래 전망)
(23, 3),
(23, 4),
(23, 6),

-- 세션 24: AI 에이전트, 글로벌 물류를 최적화하다 (산업 활용 사례, 글로벌, 미래 전망)
(24, 3),
(24, 4),
(24, 6),

-- 세션 25: 스마트 에너지 시대: AI 에이전트의 역할 (산업 활용 사례, 미래 전망)
(25, 3),
(25, 6),

-- 세션 26: AI 에이전트, 맞춤형 교육을 실현하다 (산업 활용 사례, 미래 전망)
(26, 3),
(26, 6),

-- 세션 27: AI 에이전트, 고객 경험을 혁신하다 (산업 활용 사례, 미래 전망)
(27, 3),
(27, 6),

-- SessionKeyword 데이터 삽입 (15:30 ~ 16:30 - AI 에이전트의 윤리 및 사회적 영향: 토론)
-- 세션 28: AI 에이전트와 미래 사회: 윤리, 정책/법, 지속가능성 (미래 전망)
(28, 6);