# Kotlin에서 동시성 제어 방식 및 각 적용의 장/단점

* [1. Synchronized 란?](#1-synchronized-란)
  * [1-1. Synchronized의 두 가지 형태](#1-1-synchronized의-두-가지-형태)
  * [1-2. Synchronized의 특징](#1-2-synchronized의-특징)
* [2. ReentrantLock 이란?](#2-reentrantlock-이란)
  * [2-1. ReentrantLock의 특징](#2-1-reentrantlock의-특징)
* [3. Synchronized VS. ReentrantLock](#3-reentrantlock-vs-synchronized)
---

# **1. Synchronized 란?**
- synchronized 블록은 특정 객체에 대한 모니터 락을 획득하여 해당 블록 내의 코드가 동시에 여러 스레드에 의해 실행되지 않도록 보장합니다.
  즉, 한 스레드가 synchronized 블록에 들어가면 다른 스레드는 해당 블록에 들어갈 수 없습니다.

```kotlin
val lock = Any() // 동기화에 사용할 객체

fun synchronizedExample() {
    synchronized(lock) {
        // 이 블록 내의 코드는 한 번에 하나의 스레드만 접근할 수 있습니다.
        // 공유 자원에 대한 안전한 접근을 보장합니다.
    }
}
```

## 1-1. Synchronized의 두 가지 형태
### 1. @Synchronized 어노테이션
코틀린에서는 @Synchronized 어노테이션을 사용하여 메서드를 동기화할 수 있습니다.
이 경우 해당 메서드는 인스턴스에 대한 락을 획득합니다.

```kotlin
class Example {
    @Synchronized
    fun synchronizedMethod() {
        // 동기화된 코드
    }
}
```

<br>

### 2. synchronized 블록
또 다른 방법은 synchronized 블록을 사용하는 것입니다.
이 방법은 특정 객체에 대한 락을 명시적으로 획득할 수 있습니다.

#### 1. lock

```kotlin
val lock = Any() // 동기화에 사용할 객체

fun synchronizedExample() {
    synchronized(lock) {
      // 동기화된 코드
    }
}
```

lock은 동기화에 사용할 객체로, 일반적으로 Any()와 같은 새로운 객체를 생성하여 사용합니다.
특정한 자원이나 코드 블록에 대한 동기화를 위해 독립적인 락을 제공합니다.
이 경우 lock 객체는 다른 코드에서 사용되지 않기 때문에, 해당 블록에 대한 접근을 안전하게 제어할 수 있습니다.

<br>

#### 2. this

```kotlin
fun synchronizedBlock() {
    synchronized(this) {
      // 동기화된 코드
    }
}
```

this는 현재 객체 인스턴스를 참조합니다.
클래스의 인스턴스에 대한 동기화를 제공합니다. 즉, 동일한 인스턴스의 메서드가 동시에 호출되지 않도록 보장합니다.

<br>

## **1-2. Synchronized의 특징**

1. 모니터 락( = 고유 락)
    - synchronized는 특정 객체에 대한 모니터 락을 사용합니다. 이 객체는 synchronized 블록에 대한 접근을 제어합니다.
2. 스레드 안전성
    - synchronized 블록 내의 코드는 한 번에 하나의 스레드만 실행할 수 있으므로, 공유 자원에 대한 안전한 접근을 보장합니다.
3. 성능
    - synchronized는 락을 획득하고 해제하는 오버헤드가 있으므로, 성능에 영향을 줄 수 있습니다. 따라서 필요한 경우에만 사용해야 합니다.
4. 중첩 사용
    - synchronized 블록은 중첩될 수 있으며, 동일한 락을 사용하는 경우에는 재진입이 가능합니다.

<br>

# 2. ReentrantLock 이란?

- ReentrantLock은 자바에서 동기화를 구현하는 데 사용되는 클래스입니다.
- synchronized 키워드와 유사한 기능을 제공하지만 더 많은 유연성과 기능을 제공합니다.
- lock()을 호출하여 락을 획득하고, unlock()을 호출하여 락을 해제합니다.
- 수동적으로 Lock 을 생성하고 해제해야 하기 때문에 finally 안에 unlock을 선언하는 것이 안전합니다.

```kotlin
import java.util.concurrent.locks.ReentrantLock

class Example {
    private val lock = ReentrantLock()
    
    fun performTask() {
        lock.lock() // 락 획득
        try {
            // 보호되어야 할 코드 영역
            // 임계 영역 (Critical Section)
        } finally {
            lock.unlock() // 락 해제
        }
    }
}
```

<br>

## 2-1. ReentrantLock의 특징

### 1. Reentrant (재진입성)
ReentrantLock에서의 재진입성(reentrancy)은 동일한 스레드가 이미 획득한 잠금을 다시 획득할 수 있는 능력을 의미합니다.  
즉, 한 스레드가 잠금을 소유하고 있는 동안 그 스레드가 같은 잠금을 다시 요청할 수 있으며, 이 경우 잠금이 해제될 때까지 다른 스레드는 해당 잠금을 획득할 수 없습니다.  

<br>

#### 재진입성의 특징
1. 동일한 스레드에 의한 재진입
   - 동일한 스레드가 잠금을 여러 번 획득할 수 있습니다.
   - 이 경우 잠금을 해제하기 위해서는 획득한 횟수만큼 unlock()을 호출해야 합니다.
2. 스레드 안전성
   - 재진입성 덕분에 잠금을 소유한 스레드가 다른 메서드를 호출할 때, 해당 메서드가 다시 같은 잠금을 요청하더라도 교착 상태에 빠지지 않습니다.

<br>

#### 재진입성 예시

```kotlin
import java.util.concurrent.locks.ReentrantLock

class Counter {
    private val lock = ReentrantLock()
    private var count = 0
    
    fun increment() {
        lock.lock()
        try {
            count++
            // 재진입 호출
            incrementAgain()
        } finally {
            lock.unlock()
        }
    }

    private fun incrementAgain() {
        lock.lock() // 동일한 스레드가 잠금을 다시 요청
        try {
            count++
        } finally {
            lock.unlock()
        }
    }
    
    fun getCount(): Int {
        return count
    }
}

fun main() {
    val counter = Counter()
    counter.increment()
    println("Count: ${counter.getCount()}") // Count: 2
}

```

1. Counter 클래스는 ReentrantLock을 사용하여 count 변수를 보호합니다.
2. increment() 메서드는 잠금을 획득하고 count를 증가시킵니다. 그 후, incrementAgain() 메서드를 호출합니다.
3. incrementAgain() 메서드 내에서도 잠금을 다시 요청합니다. 하지만 이 경우, 동일한 스레드가 잠금을 요청하므로, 재진입이 허용됩니다.
4. 최종적으로 count는 2가 됩니다.

재진입성이 없었다면 incrementAgain() 메서드에서 잠금을 요청할 때 교착 상태가 발생할 수 있었지만, ReentrantLock 덕분에 안전하게 동작합니다.

<br>

#### 교착 상태(Deadlock)와 재진입성
교착 상태는 두 개 이상의 스레드가 서로의 자원을 기다리며 무한 대기 상태에 빠지는 상황을 말합니다.  
예를 들어, 스레드 A가 스레드 B가 소유한 자원을 기다리고, 동시에 스레드 B가 스레드 A가 소유한 자원을 기다리는 경우입니다.

- 재진입성이 없는 경우
  - 재진입성이 없는 잠금 메커니즘을 사용한다고 가정해 보겠습니다.
    1. 스레드 A가 lockA를 획득합니다.
    2. 스레드 A가 methodA()를 호출하고, 이 메서드 내에서 lockA를 다시 요청합니다.
    3. 이 경우 스레드 A는 이미 lockA를 소유하고 있지만, 재진입성이 없기 때문에 lockA를 다시 획득할 수 없습니다. 따라서 스레드 A는 대기 상태에 빠지게 됩니다.

  이런 상황에서는 스레드 A가 lockA를 해제하지 않기 때문에, 스레드 B가 lockA를 기다리게 되면 교착 상태가 발생할 수 있습니다.

<br>

- 재진입성이 있는 경우
  - 반면, ReentrantLock을 사용하면 다음과 같은 방식으로 동작합니다.
    1. 스레드 A가 lockA를 획득합니다.
    2. 스레드 A가 methodA()를 호출하고, 이 메서드 내에서 lockA를 다시 요청합니다.
    3. 이 경우 스레드 A는 이미 lockA를 소유하고 있으므로, 재진입성이 허용되어 lockA를 다시 획득할 수 있습니다. 따라서 스레드 A는 계속해서 작업을 수행할 수 있습니다.

  이렇게 되면 스레드 A는 lockA를 해제할 때까지 다른 스레드가 lockA를 획득할 수 없지만, 스레드 A는 자신의 작업을 계속 진행할 수 있습니다. 결과적으로 교착 상태에 빠지지 않게 됩니다.

<br>

### 2. Fairness (공정성):
락을 획득하기 위해 대기하는 스레드들의 순서를 관리할 수 있습니다.  
ReentrantLock 생성 시 fair 파라미터를 true로 설정하면, 락을 요청한 순서대로 락을 획득할 수 있도록 보장합니다.  

<br>

#### 1. 공정 모드 (Fair Mode)
공정 모드에서는 락을 요청한 스레드가 대기 큐에 들어간 순서에 따라 락을 획득합니다.  
즉, 먼저 요청한 스레드가 먼저 락을 획득하는 방식입니다.  
이 모드는 스레드가 락을 기다리는 시간을 최소화하고, starvation(기아 상태)을 방지하는 데 도움이 됩니다.  

```kotlin
val lock = ReentrantLock(true)
```

공정 모드를 사용하려면 ReentrantLock을 생성할 때 true를 인자로 전달해야 합니다.  

<br>

#### 2. 비공정 모드 (Non-Fair Mode):
비공정 모드에서는 락을 요청한 순서와 관계없이 락을 획득할 수 있습니다.  
즉, 락을 요청한 스레드가 대기 중인 다른 스레드보다 먼저 락을 획득할 수 있는 경우가 있습니다.  
이 모드는 성능이 더 좋을 수 있으며, 락을 자주 획득하고 해제하는 경우에 유리할 수 있습니다. 그러나 starvation이 발생할 가능성이 있습니다.  

```kotlin
val lock = ReentrantLock()
```

비공정 모드는 기본값이며, 생성자에 인자를 전달하지 않으면 비공정 모드로 생성됩니다.

<br>

#### 공정 VS. 비공정
- 공정 모드
  - 장점
    - 모든 스레드가 공평하게 락을 획득할 수 있으므로, 특정 스레드가 계속해서 락을 획득하지 못하는 상황을 방지할 수 있습니다.
    - 대기 중인 스레드가 공정하게 락을 획득하므로, 대기 시간이 예측 가능해집니다.
  - 단점
    - 성능이 저하될 수 있습니다. 공정 모드는 스레드가 락을 획득하기 위해 대기하는 동안 추가적인 오버헤드가 발생할 수 있습니다.
    - 락을 자주 획득하고 해제하는 경우, 비공정 모드보다 성능이 떨어질 수 있습니다.
  
<br>

- 비공정 모드
  - 장점
    - 성능이 더 좋을 수 있으며, 락을 자주 획득하고 해제하는 경우에 유리합니다.
    - 스레드가 락을 획득하는 데 더 빠를 수 있습니다.
  - 단점
    - 특정 스레드가 계속해서 락을 획득하지 못하는 starvation 문제가 발생할 수 있습니다.
    - 대기 중인 스레드의 대기 시간이 예측 불가능할 수 있습니다.

<br>

공정 모드는 다수의 스레드가 락을 공유해서 공정성을 중시하는 경우에 적합하며, 비공정 모드는 성능을 중시하거나 스레드 간의 경쟁이 적은 환경에 적합합니다.

<br>

### 3. 유연성

#### 1. 락의 획득 및 해제 제어

- ReentrantLock은 락을 명시적으로 획득하고 해제할 수 있는 메서드를 제공합니다.
- lock() 메서드를 사용하여 락을 획득하고, unlock() 메서드를 사용하여 락을 해제합니다.
- 개발자가 락의 획득과 해제를 세밀하게 제어할 수 있게 해줍니다.

#### 2. 타임아웃 기능

- ReentrantLock은 락을 획득할 때 타임아웃을 설정할 수 있는 기능을 제공합니다.
- tryLock() 메서드를 사용하면 락을 즉시 획득할 수 없는 경우 대기하지 않고 false를 반환합니다.
- tryLock(long timeout, TimeUnit unit) 메서드를 사용하면 지정된 시간 동안 락 획득을 시도하고, 시간 초과 시 false를 반환합니다.

<br>

### 4. Condition (조건)

Condition은 ReentrantLock과 함께 사용되어 스레드 간의 복잡한 동기화를 가능하게 하는 기능입니다.  
Condition을 통해 스레드는 특정 조건이 만족될 때까지 대기하고, 다른 스레드가 해당 조건을 만족시키면 대기 중인 스레드를 깨울 수 있습니다.  

<br>

#### Condition 객체 생성 및 사용법

- Condition 객체 생성  
  ReentrantLock 객체에서 newCondition() 메서드를 호출하여 Condition 객체를 생성합니다.

  ```kotlin
  val lock = ReentrantLock()
  val condition: Condition = lock.newCondition()
  ```

- await() 메서드  
  스레드가 특정 조건을 기다리기 위해 await() 메서드를 호출합니다. 이 메서드는 락을 해제하고 스레드를 대기 상태로 만듭니다.

  ```kotlin
  lock.lock()
  try {
      while (!condition이_만족되었는지_확인하는_조건) {
          condition.await()
      }
  
      // 조건이 만족되었을 때 실행할 코드
  
  } finally {
      lock.unlock()
  }
  ```

- signal() 및 signalAll() 메서드  
  다른 스레드가 조건을 만족시켰을 때, signal() 메서드는 대기 중인 스레드 중 하나를 깨우고, signalAll() 메서드는 모든 대기 중인 스레드를 깨웁니다.

  ```kotlin
  lock.lock()
  try {
      
      // 조건 만족시키는 코드
      
      condition.signalAll() // 또는 condition.signal();
  } finally {
      lock.unlock()
  }
  ```

<br>

#### Condition 예시

```kotlin
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class ConditionExample {
  private val lock = ReentrantLock()
  private val condition: Condition = lock.newCondition()
  private var count = 0

  fun increment() {
    lock.lock()
    try {
      count++
      println("Count incremented to: $count")
      // 조건이 충족되면 대기 중인 스레드를 깨웁니다.
      if (count == 5) {
        condition.signalAll() // count가 5가 되면 대기 중인 스레드를 깨웁니다.
      }
    } finally {
      lock.unlock()
    }
  }

  fun awaitCondition() {
    lock.lock()
    try {
      while (count < 5) {
        println("Waiting for count to reach 5...")
        condition.await() // count가 5가 될 때까지 대기합니다.
      }
      println("Count reached 5, proceeding...")
    } catch (e: InterruptedException) {
      Thread.currentThread().interrupt() // 인터럽트 상태 복원
    } finally {
      lock.unlock()
    }
  }
}

fun main() {
  val example = ConditionExample()

  // 스레드 생성
  val incrementer = Thread {
    for (i in 0 until 10) {
      example.increment()
      try {
        Thread.sleep(100) // 인크리먼트 간의 지연
      } catch (e: InterruptedException) {
        Thread.currentThread().interrupt()
      }
    }
  }

  val waiter = Thread {
    example.awaitCondition()
  }

  // 스레드 시작
  waiter.start()
  incrementer.start()
}
```

main 메서드가 실행되면, ConditionExample 객체가 생성되고, 두 개의 스레드(incrementer와 waiter)가 생성됩니다.  
waiter 스레드가 먼저 시작되어 awaitCondition()을 호출하고 count가 5에 도달할 때까지 대기합니다.  
그 후 incrementer 스레드가 시작되어 increment() 메서드를 호출하여 count를 증가시키기 시작합니다.  
이 두 스레드는 동시에 실행되며, incrementer 스레드가 count를 증가시키는 동안 waiter 스레드는 count가 5에 도달할 때까지 대기합니다.  
count가 5에 도달하면 incrementer 스레드에서 signalAll()이 호출되어 waiter 스레드가 대기에서 깨어나고, 이후의 작업을 계속 진행합니다.  

- 출력 결과

  ```text
  Waiting for count to reach 5...
  Count incremented to: 1
  Count incremented to: 2
  Count incremented to: 3
  Count incremented to: 4
  Count incremented to: 5
  Count reached 5, proceeding...
  Count incremented to: 6
  Count incremented to: 7
  Count incremented to: 8
  Count incremented to: 9
  Count incremented to: 10
  ```

<br>

# 3. ReentrantLock VS. synchronized

| 특징 | synchronized | ReentrantLock |
| --- | --- | --- |
| 락 | 암시적 | 명시적 |
| 재진입성 | 지원 | 지원 |
| 공정성 | 지원 안 함 | 지원 (선택적) |
| 유연성 | 낮음 | 높음 |
| Condition | 지원 (wait, notify, notifyAll) | 지원 (Condition 인터페이스) |
| 예외 처리 | 락 자동 해제 | finally 블록에서 락 해제 필요 |
| 성능 | 상황에 따라 다름 | 일반적으로 synchronized 보다 좋음 |

코드가 간단하고, 예외 처리에 대한 걱정이 없는 경우 synchronized를 사용하는 것이 좋습니다.  
공정성, 타임아웃, 조건 변수 등의 기능이 필요한 경우 ReentrantLock을 사용하는 것이 더 적합합니다.

<br>

# Reference

- [https://seodeveloper.tistory.com/entry/JAVA-ReentrantLock-이란](https://seodeveloper.tistory.com/entry/JAVA-ReentrantLock-%EC%9D%B4%EB%9E%80)
- [https://velog.io/@picbel/Synchronized와-ReentrantLock의-차이](https://velog.io/@picbel/Synchronized%EC%99%80-ReentrantLock%EC%9D%98-%EC%B0%A8%EC%9D%B4)