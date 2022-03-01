package org.example.zhc;

import com.syyx.tpf.service.utils.callback.ObjCallBack;
import com.syyx.tpf.service.utils.lock.IdLocker;
import com.syyx.tpf.service.utils.tuple.*;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.*;

@Slf4j
public class TpfPromise<T> extends Mono<T> {
  public static final BiFunction<Integer, Class<? extends Throwable>, Predicate<Throwable>>
      matchRetryFun =
          ((retries, exception) ->
              (throwable) -> {
                if (retries <= 0) {
                  return false;
                }
                return exception.isInstance(throwable);
              });
  public static final BiFunction<
          Integer, Collection<Class<? extends Throwable>>, Predicate<Throwable>>
      anyMatchRetryFun =
          ((retries, exceptions) ->
              (throwable) -> {
                if (retries <= 0 || exceptions.isEmpty()) {
                  return false;
                }
                return exceptions.stream().anyMatch(exception -> exception.isInstance(throwable));
              });
  private static final IdLocker lockForZipToOne = new IdLocker();
  private static final ConcurrentHashMap<String, ConcurrentLinkedDeque<ObjCallBack<Object>>>
      zipToOneContext = new ConcurrentHashMap<>();
  private final Mono<T> mono;

  public TpfPromise(Mono<T> mono) {
    this.mono = mono;
  }

  private static <T> com.syyx.tpf.service.utils.TpfPromise<T> warp(Mono<T> mono) {
    return new com.syyx.tpf.service.utils.TpfPromise<>(mono);
  }

  /**
   * Creates a deferred emitter that can be used with callback-based APIs to signal at most one
   * value, a complete or an error signal.
   *
   * <p><img class="marble" src="doc-files/marbles/createForMono.svg" alt="">
   *
   * <p>Bridging legacy API involves mostly boilerplate code due to the lack of standard types and
   * methods. There are two kinds of API surfaces: 1) addListener/removeListener and 2)
   * callback-handler.
   *
   * <p><b>1) addListener/removeListener pairs</b><br>
   * To work with such API one has to instantiate the listener, call the sink from the listener then
   * register it with the source:
   *
   * <pre><code>
   * TpfPromise.&lt;String&gt;create(sink -&gt; {
   *     HttpListener listener = event -&gt; {
   *         if (event.getResponseCode() >= 400) {
   *             sink.error(new RuntimeException("Failed"));
   *         } else {
   *             String body = event.getBody();
   *             if (body.isEmpty()) {
   *                 sink.success();
   *             } else {
   *                 sink.success(body.toLowerCase());
   *             }
   *         }
   *     };
   *
   *     client.addListener(listener);
   *
   *     sink.onDispose(() -&gt; client.removeListener(listener));
   * });
   * </code></pre>
   *
   * Note that this works only with single-value emitting listeners. Otherwise, all subsequent
   * signals are dropped. You may have to add {@code client.removeListener(this);} to the listener's
   * body.
   *
   * <p><b>2) callback handler</b><br>
   * This requires a similar instantiation pattern such as above, but usually the successful
   * completion and error are separated into different methods. In addition, the legacy API may or
   * may not support some cancellation mechanism.
   *
   * <pre><code>
   * TpfPromise.&lt;String&gt;create(sink -&gt; {
   *     Callback&lt;String&gt; callback = new Callback&lt;String&gt;() {
   *         &#64;Override
   *         public void onResult(String data) {
   *             sink.success(data.toLowerCase());
   *         }
   *
   *         &#64;Override
   *         public void onError(Exception e) {
   *             sink.error(e);
   *         }
   *     }
   *
   *     // without cancellation support:
   *
   *     client.call("query", callback);
   *
   *     // with cancellation support:
   *
   *     AutoCloseable cancel = client.call("query", callback);
   *     sink.onDispose(() -> {
   *         try {
   *             cancel.close();
   *         } catch (Exception ex) {
   *             Exceptions.onErrorDropped(ex);
   *         }
   *     });
   * });
   * </code></pre>
   *
   * @param callback Consume the {@link TpfPromiseSink} provided per-subscriber by Reactor to
   *     generate signals.
   * @param <T> The type of the value emitted
   * @return a {@link com.syyx.tpf.service.utils.TpfPromise}
   */
  public static <T> com.syyx.tpf.service.utils.TpfPromise<T> warpCallback(ConsumerWithException<TpfPromiseSink<T>> callback) {
    Consumer<MonoSink<T>> consumer =
        tMonoSink -> {
          try {
            TpfPromiseSink<T> tTpfPromiseSink = new TpfPromiseSink<>(tMonoSink);
            callback.accept(tTpfPromiseSink);
          } catch (Throwable e) {
            tMonoSink.error(e);
          }
        };
<<<<<<< HEAD
        return com.syyx.tpf.service.utils.TpfPromise.warp(Mono.create(consumer));
    }

    public static <T> com.syyx.tpf.service.utils.TpfPromise<T> warpSupplier(Supplier<T> supplier) {
        return com.syyx.tpf.service.utils.TpfPromise.warp(result(supplier.get()));
    }


    public static <T> com.syyx.tpf.service.utils.TpfPromise<T> warpCallAble(Supplier<Mono<T>> supplier) {
        return com.syyx.tpf.service.utils.TpfPromise.warp(Mono.defer(supplier));
    }

    /**
     * Let this {@link com.syyx.tpf.service.utils.TpfPromise} complete then play another Mono.
     * <p>
     * In other words ignore element from this {@link com.syyx.tpf.service.utils.TpfPromise} and transform its completion signal into the
     * emission and completion signal of a provided {@code Mono<V>}. Error signal is
     * replayed in the resulting {@code Mono<V>}.
     *
     * <p>
     * <img class="marble" src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/ignorethen1.png" alt="">
     *
     * @param other a {@link com.syyx.tpf.service.utils.TpfPromise} to emit from after termination
     * @param <V>   the element type of the supplied Mono
     * @return a new {@link com.syyx.tpf.service.utils.TpfPromise} that emits from the supplied {@link com.syyx.tpf.service.utils.TpfPromise}
     */

    public <V> com.syyx.tpf.service.utils.TpfPromise<V> justThen(Mono<V> other) {
        return com.syyx.tpf.service.utils.TpfPromise.warp(mono.then(other));
    }

    /**
     * Let this {@link com.syyx.tpf.service.utils.TpfPromise} complete then play another Mono.
     * <p>
     * In other words ignore element from this {@link com.syyx.tpf.service.utils.TpfPromise} and transform its completion signal into the
     * emission and completion signal of a provided {@code Mono<V>}. Error signal is
     * replayed in the resulting {@code Mono<V>}.
     *
     * <p>
     * <img class="marble" src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/ignorethen1.png" alt="">
     *
     * @param supplier a {@link com.syyx.tpf.service.utils.TpfPromise} to emit from after termination
     * @param <V>      the element type of the supplied Mono
     * @return a new {@link com.syyx.tpf.service.utils.TpfPromise} that emits from the supplied {@link com.syyx.tpf.service.utils.TpfPromise}
     */

    public <V> com.syyx.tpf.service.utils.TpfPromise<V> justThen(Supplier<Mono<V>> supplier) {
        return justThen(com.syyx.tpf.service.utils.TpfPromise.warpCallAble(supplier));
    }


    public com.syyx.tpf.service.utils.TpfPromise<T> ifEmptyThen(Supplier<? extends Mono<? extends T>> supplier) {
        return ifEmptyThen(com.syyx.tpf.service.utils.TpfPromise.warpCallback(
                vTpfPromiseSink ->
                        supplier.get()
                                .subscribe(
                                        rel -> vTpfPromiseSink.success(rel),
                                        vTpfPromiseSink::error
                                )
                )
        );
    }


    /**
     * Fallback to an alternative {@link com.syyx.tpf.service.utils.TpfPromise} if this mono is completed without data
     *
     * <p>
     * <img class="marble" src="doc-files/marbles/switchIfEmptyForMono.svg" alt="">
     *
     * @param alternate the alternate mono if this TpfPromise is empty
     * @return a {@link com.syyx.tpf.service.utils.TpfPromise} falling back upon source completing without elements
     */

    public final com.syyx.tpf.service.utils.TpfPromise<T> ifEmptyThen(Mono<? extends T> alternate) {
        return com.syyx.tpf.service.utils.TpfPromise.warp(mono.switchIfEmpty(alternate));
    }

    /**
     * Create a {@link com.syyx.tpf.service.utils.TpfPromise} that completes without emitting any item.
     *
     * <p>
     * <img class="marble" src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/empty.png" alt="">
     * <p>
     *
     * @param <T> the reified {@link Subscriber} type
     * @return a completed {@link com.syyx.tpf.service.utils.TpfPromise}
     */

    public static <T> com.syyx.tpf.service.utils.TpfPromise<T> result() {
        return com.syyx.tpf.service.utils.TpfPromise.warp(Mono.empty());
    }


    /**
     * Create a {@link com.syyx.tpf.service.utils.TpfPromise} that completes without emitting any item.
     *
     * <p>
     * <img class="marble" src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/empty.png" alt="">
     * <p>
     *
     * @return a completed {@link com.syyx.tpf.service.utils.TpfPromise}
     */
=======
    return com.syyx.tpf.service.utils.TpfPromise.warp(Mono.create(consumer));
  }
>>>>>>> dev

  public TpfPromise<T> zipCalling(String tag){
      return zipToBeforeCalling(tag, ()->this);
  }

    /**
     * 将呼叫压缩到之前的tag
     * 如果之前有未完成的调用，就不用发起新的调用，等待之前的调用返回时统一返回
     * @param tag tag
     * @param supplier 提供一个异步请求
     * @param <T> 返回值类型参数
     * @return 异步调用句柄
     */
  public static <T> TpfPromise<T> zipToBeforeCalling(String tag, Supplier<TpfPromise<T>> supplier) {
    ConsumerWithException<TpfPromiseSink<T>> consumer =
        sink -> {
          ObjCallBack<Object> retFun =
              new ObjCallBack<Object>() {
                @Override
                public void onRet(Object o) {
                  sink.success((T) o);
                }

                @Override
                public void onError(Throwable throwable) {
                  sink.error(throwable);
                }
              };
          log.info("zipToBeforeCalling tag_{} call", tag);
          lockForZipToOne.lock(
              tag,
              () -> {
                CallQueue callQueue = CallQueueMgr.getInstance().getLocalQueue();
                // 到这里为止都是同步的， 为每个对象的读取请求创建一个专属的回调队列
                ConcurrentLinkedDeque<ObjCallBack<Object>> concurrentLinkedDeque =
                    zipToOneContext.computeIfAbsent(tag, newTag -> new ConcurrentLinkedDeque<>());
                  concurrentLinkedDeque.add(retFun);
                if (concurrentLinkedDeque.size() == 1) {
                    fromSuppier(supplier)
                      .consumerValue(
                          rel -> {
                              Callable<ConcurrentLinkedDeque<ObjCallBack<Object>>> callable = ()-> zipToOneContext.remove(tag);
                              ConcurrentLinkedDeque<ObjCallBack<Object>> objCallBacks;
                              // 如果不在同一个线程就需要再加一次锁，否则就不用枷锁
                              if (callQueue != CallQueueMgr.getInstance().getLocalQueue()) {
                                objCallBacks = lockForZipToOne.lockCallable(tag, callable);
                              } else {
                                  objCallBacks = callable.call();
                              }
                              objCallBacks.forEach(objectObjCallBack -> {
                                  log.info("zipToBeforeCalling tag_{} call ret {}", tag, rel);
                                  objectObjCallBack.ret(rel);
                              });
                          })
                      .catchError(
                              throwable -> {
                                  ConcurrentLinkedDeque<ObjCallBack<Object>> objCallBacks;
                                  // 如果不在同一个线程就需要再加一次锁，否则就不用枷锁
                                  if (callQueue != CallQueueMgr.getInstance().getLocalQueue()) {
                                      objCallBacks = lockForZipToOne.lockCallable(tag, ()-> zipToOneContext.remove(tag));
                                  } else {
                                      objCallBacks = zipToOneContext.remove(tag);
                                  }
                                  objCallBacks.forEach(objectObjCallBack -> {
                                      log.info("zipToBeforeCalling tag_{} call error!", tag, throwable);
                                      objectObjCallBack.error(throwable);
                                  });
                              })
                      .start();
                } else {
                    log.info("排队等待 zipToBeforCalling tag_{}", tag);
                }
              });
        };
    return TpfPromise.warpCallback(consumer);
  }

  public static <T> TpfPromise<T> warpSupplier(Supplier<T> supplier) {
    return TpfPromise.warp(result(supplier.get()));
  }

    public static <T> TpfPromise<T> fromSuppier(Supplier<TpfPromise<T>> supplier) {
        return TpfPromise.warp(Mono.defer(supplier));
    }


    public static <T> TpfPromise<T> warpCallAble(Supplier<Mono<T>> supplier) {
    return TpfPromise.warp(Mono.defer(supplier));
  }

  /**
   * Create a {@link TpfPromise} that completes without emitting any item.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/empty.png"
   * alt="">
   *
   * <p>
   *
   * @param <T> the reified {@link Subscriber} type
   * @return a completed {@link TpfPromise}
   */
  public static <T> TpfPromise<T> result() {
    return TpfPromise.warp(Mono.empty());
  }

  /**
   * Create a {@link TpfPromise} that completes without emitting any item.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/empty.png"
   * alt="">
   *
   * <p>
   *
   * @return a completed {@link TpfPromise}
   */
  public static TpfPromise<Void> resultVoid() {
    return TpfPromise.warp(Mono.empty());
  }

  /**
   * Create a {@link TpfPromise} that terminates with the specified error immediately after being
   * subscribed to.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/error.png"
   * alt="">
   *
   * <p>
   *
   * @param error the onError signal
   * @param <T> the reified {@link Subscriber} type
   * @return a failed {@link TpfPromise}
   */
  public static <T> TpfPromise<T> error(Throwable error) {
    return TpfPromise.warp(Mono.error(error));
  }

  /**
   * Create a new {@link TpfPromise} that emits the specified item, which is captured at
   * instantiation time.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/just.png"
   * alt="">
   *
   * <p>
   *
   * @param data the only item to onNext
   * @param <T> the type of the produced item
   * @return a {@link TpfPromise}.
   */
  public static <T> TpfPromise<T> result(T data) {
    if (data == null) {
      data = (T) Optional.empty();
    }
    return TpfPromise.warp(Mono.just(data));
  }

  /**
   * Aggregate given publishers into a new {@literal TpfPromise} that will be fulfilled when all of
   * the given {@literal sources} have completed. An error will cause pending results to be
   * cancelled and immediate error emission to the returned {@link TpfPromise}.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/when.png"
   * alt="">
   *
   * <p>
   *
   * @param sources The sources to use.
   * @return a {@link TpfPromise}.
   */
  public static @NotNull TpfPromise<Void> when(Publisher<?>... sources) {
    return TpfPromise.warp(Mono.when(sources));
  }

  /**
   * Aggregate given publishers into a new {@literal Mono} that will be fulfilled when all of the
   * given {@literal Publishers} have completed. An error will cause pending results to be cancelled
   * and immediate error emission to the returned {@link TpfPromise}.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/when.png"
   * alt="">
   *
   * <p>
   *
   * @param sources The sources to use.
   * @return a {@link TpfPromise}.
   */
  public static @NotNull TpfPromise<Void> when(final Iterable<? extends Publisher<?>> sources) {
    return TpfPromise.warp(Mono.when(sources));
  }

  /**
   * Aggregate given publishers into a new {@literal TpfPromise} that will be fulfilled when all of
   * the given {@literal sources} have completed. Errors from the sources are delayed. If several
   * Publishers error, the exceptions are combined (as suppressed exceptions on a root exception).
   *
   * <p><img class="marble" src="doc-files/marbles/whenDelayError.svg" alt="">
   *
   * <p>
   *
   * @param sources The sources to use.
   * @return a {@link TpfPromise}.
   */
  public static @NotNull TpfPromise<Void> whenDelayError(
      final Iterable<? extends Publisher<?>> sources) {
    return TpfPromise.warp(Mono.whenDelayError(sources));
  }

  /**
   * Merge given publishers into a new {@literal TpfPromise} that will be fulfilled when all of the
   * given {@literal sources} have completed. Errors from the sources are delayed. If several
   * Publishers error, the exceptions are combined (as suppressed exceptions on a root exception).
   *
   * <p><img class="marble" src="doc-files/marbles/whenDelayError.svg" alt="">
   *
   * <p>
   *
   * @param sources The sources to use.
   * @return a {@link TpfPromise}.
   */
  public static @NotNull TpfPromise<Void> whenDelayError(Publisher<?>... sources) {
    return TpfPromise.warp(Mono.whenDelayError(sources));
  }

  /**
   * Merge given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values into a {@link TpfTuple2}. An
   * error or <strong>empty</strong> completion of any source will cause other sources to be
   * cancelled and the resulting Mono to immediately error or complete, respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zip.png"
   * alt="">
   *
   * <p>
   *
   * @param p1 The first upstream {@link Publisher} to subscribe to.
   * @param p2 The second upstream {@link Publisher} to subscribe to.
   * @param <T1> type of the value from source1
   * @param <T2> type of the value from source2
   * @return a {@link TpfPromise}.
   */
  public static <T1, T2> TpfPromise<TpfTuple2<T1, T2>> all(
      Mono<? extends T1> p1, Mono<? extends T2> p2) {
    return TpfPromise.warp(Mono.zip(p1, p2))
        .nextValue(
            tuple2 ->
                TpfTuples.of(
                    OptionalUtil.nullIfEmpty(tuple2.getT1()),
                    OptionalUtil.nullIfEmpty(tuple2.getT2())));
  }

  /**
   * Merge given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values into a {@link Tuple3}. An
   * error or <strong>empty</strong> completion of any source will cause other sources to be
   * cancelled and the resulting Mono to immediately error or complete, respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zip.png"
   * alt="">
   *
   * <p>
   *
   * @param p1 The first upstream {@link Publisher} to subscribe to.
   * @param p2 The second upstream {@link Publisher} to subscribe to.
   * @param p3 The third upstream {@link Publisher} to subscribe to.
   * @param <T1> type of the value from source1
   * @param <T2> type of the value from source2
   * @param <T3> type of the value from source3
   * @return a {@link TpfPromise}.
   */
  public static <T1, T2, T3> TpfPromise<TpfTuple3<T1, T2, T3>> all(
      Mono<? extends T1> p1, Mono<? extends T2> p2, Mono<? extends T3> p3) {
    return TpfPromise.warp(Mono.zip(p1, p2, p3))
        .nextValue(
            tuple3 ->
                TpfTuples.of(
                    OptionalUtil.nullIfEmpty(tuple3.getT1()),
                    OptionalUtil.nullIfEmpty(tuple3.getT2()),
                    OptionalUtil.nullIfEmpty(tuple3.getT3())));
  }

  /**
   * Merge given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values into a {@link Tuple4}. An
   * error or <strong>empty</strong> completion of any source will cause other sources to be
   * cancelled and the resulting Mono to immediately error or complete, respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zip.png"
   * alt="">
   *
   * <p>
   *
   * @param p1 The first upstream {@link Publisher} to subscribe to.
   * @param p2 The second upstream {@link Publisher} to subscribe to.
   * @param p3 The third upstream {@link Publisher} to subscribe to.
   * @param p4 The fourth upstream {@link Publisher} to subscribe to.
   * @param <T1> type of the value from source1
   * @param <T2> type of the value from source2
   * @param <T3> type of the value from source3
   * @param <T4> type of the value from source4
   * @return a {@link TpfPromise}.
   */
  public static <T1, T2, T3, T4> TpfPromise<TpfTuple4<T1, T2, T3, T4>> all(
      Mono<? extends T1> p1, Mono<? extends T2> p2, Mono<? extends T3> p3, Mono<? extends T4> p4) {
    return TpfPromise.warp(Mono.zip(p1, p2, p3, p4))
        .nextValue(
            tuple4 ->
                TpfTuples.of(
                    OptionalUtil.nullIfEmpty(tuple4.getT1()),
                    OptionalUtil.nullIfEmpty(tuple4.getT2()),
                    OptionalUtil.nullIfEmpty(tuple4.getT3()),
                    OptionalUtil.nullIfEmpty(tuple4.getT4())));
  }

  /**
   * Merge given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values into a {@link Tuple5}. An
   * error or <strong>empty</strong> completion of any source will cause other sources to be
   * cancelled and the resulting Mono to immediately error or complete, respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zip.png"
   * alt="">
   *
   * <p>
   *
   * @param p1 The first upstream {@link Publisher} to subscribe to.
   * @param p2 The second upstream {@link Publisher} to subscribe to.
   * @param p3 The third upstream {@link Publisher} to subscribe to.
   * @param p4 The fourth upstream {@link Publisher} to subscribe to.
   * @param p5 The fifth upstream {@link Publisher} to subscribe to.
   * @param <T1> type of the value from source1
   * @param <T2> type of the value from source2
   * @param <T3> type of the value from source3
   * @param <T4> type of the value from source4
   * @param <T5> type of the value from source5
   * @return a {@link TpfPromise}.
   */
  public static <T1, T2, T3, T4, T5> TpfPromise<TpfTuple5<T1, T2, T3, T4, T5>> all(
      Mono<? extends T1> p1,
      Mono<? extends T2> p2,
      Mono<? extends T3> p3,
      Mono<? extends T4> p4,
      Mono<? extends T5> p5) {
    return TpfPromise.warp(Mono.zip(p1, p2, p3, p4, p5))
        .nextValue(
            tuple5 ->
                TpfTuples.of(
                    OptionalUtil.nullIfEmpty(tuple5.getT1()),
                    OptionalUtil.nullIfEmpty(tuple5.getT2()),
                    OptionalUtil.nullIfEmpty(tuple5.getT3()),
                    OptionalUtil.nullIfEmpty(tuple5.getT4()),
                    OptionalUtil.nullIfEmpty(tuple5.getT5())));
  }

  /**
   * Merge given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values into a {@link Tuple6}. An
   * error or <strong>empty</strong> completion of any source will cause other sources to be
   * cancelled and the resulting Mono to immediately error or complete, respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zip.png"
   * alt="">
   *
   * <p>
   *
   * @param p1 The first upstream {@link Publisher} to subscribe to.
   * @param p2 The second upstream {@link Publisher} to subscribe to.
   * @param p3 The third upstream {@link Publisher} to subscribe to.
   * @param p4 The fourth upstream {@link Publisher} to subscribe to.
   * @param p5 The fifth upstream {@link Publisher} to subscribe to.
   * @param p6 The sixth upstream {@link Publisher} to subscribe to.
   * @param <T1> type of the value from source1
   * @param <T2> type of the value from source2
   * @param <T3> type of the value from source3
   * @param <T4> type of the value from source4
   * @param <T5> type of the value from source5
   * @param <T6> type of the value from source6
   * @return a {@link TpfPromise}.
   */
  public static <T1, T2, T3, T4, T5, T6> TpfPromise<TpfTuple6<T1, T2, T3, T4, T5, T6>> all(
      Mono<? extends T1> p1,
      Mono<? extends T2> p2,
      Mono<? extends T3> p3,
      Mono<? extends T4> p4,
      Mono<? extends T5> p5,
      Mono<? extends T6> p6) {
    return TpfPromise.warp(Mono.zip(p1, p2, p3, p4, p5, p6))
        .nextValue(
            tuple6 ->
                TpfTuples.of(
                    OptionalUtil.nullIfEmpty(tuple6.getT1()),
                    OptionalUtil.nullIfEmpty(tuple6.getT2()),
                    OptionalUtil.nullIfEmpty(tuple6.getT3()),
                    OptionalUtil.nullIfEmpty(tuple6.getT4()),
                    OptionalUtil.nullIfEmpty(tuple6.getT5()),
                    OptionalUtil.nullIfEmpty(tuple6.getT6())));
  }

  /**
   * Merge given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values into a {@link Tuple6}. An
   * error or <strong>empty</strong> completion of any source will cause other sources to be
   * cancelled and the resulting Mono to immediately error or complete, respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zip.png"
   * alt="">
   *
   * <p>
   *
   * @param p1 The first upstream {@link Publisher} to subscribe to.
   * @param p2 The second upstream {@link Publisher} to subscribe to.
   * @param p3 The third upstream {@link Publisher} to subscribe to.
   * @param p4 The fourth upstream {@link Publisher} to subscribe to.
   * @param p5 The fifth upstream {@link Publisher} to subscribe to.
   * @param p6 The sixth upstream {@link Publisher} to subscribe to.
   * @param <T1> type of the value from source1
   * @param <T2> type of the value from source2
   * @param <T3> type of the value from source3
   * @param <T4> type of the value from source4
   * @param <T5> type of the value from source5
   * @param <T6> type of the value from source6
   * @param <T7> type of the value from source7
   * @return a {@link TpfPromise}.
   */
  public static <T1, T2, T3, T4, T5, T6, T7> TpfPromise<TpfTuple7<T1, T2, T3, T4, T5, T6, T7>> all(
      Mono<? extends T1> p1,
      Mono<? extends T2> p2,
      Mono<? extends T3> p3,
      Mono<? extends T4> p4,
      Mono<? extends T5> p5,
      Mono<? extends T6> p6,
      Mono<? extends T7> p7) {
    return TpfPromise.warp(Mono.zip(p1, p2, p3, p4, p5, p6, p7))
        .nextValue(
            tuple7 ->
                TpfTuples.of(
                    OptionalUtil.nullIfEmpty(tuple7.getT1()),
                    OptionalUtil.nullIfEmpty(tuple7.getT2()),
                    OptionalUtil.nullIfEmpty(tuple7.getT3()),
                    OptionalUtil.nullIfEmpty(tuple7.getT4()),
                    OptionalUtil.nullIfEmpty(tuple7.getT5()),
                    OptionalUtil.nullIfEmpty(tuple7.getT6()),
                    OptionalUtil.nullIfEmpty(tuple7.getT7())));
  }

  /**
   * Merge given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values into a {@link TpfTuple6}. An
   * error or <strong>empty</strong> completion of any source will cause other sources to be
   * cancelled and the resulting Mono to immediately error or complete, respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zip.png"
   * alt="">
   *
   * <p>
   *
   * @param p1 The first upstream {@link Publisher} to subscribe to.
   * @param p2 The second upstream {@link Publisher} to subscribe to.
   * @param p3 The third upstream {@link Publisher} to subscribe to.
   * @param p4 The fourth upstream {@link Publisher} to subscribe to.
   * @param p5 The fifth upstream {@link Publisher} to subscribe to.
   * @param p6 The sixth upstream {@link Publisher} to subscribe to.
   * @param <T1> type of the value from source1
   * @param <T2> type of the value from source2
   * @param <T3> type of the value from source3
   * @param <T4> type of the value from source4
   * @param <T5> type of the value from source5
   * @param <T6> type of the value from source6
   * @param <T7> type of the value from source7
   * @param <T8> type of the value from source8
   * @return a {@link TpfPromise}.
   */
  public static <T1, T2, T3, T4, T5, T6, T7, T8>
      TpfPromise<TpfTuple8<T1, T2, T3, T4, T5, T6, T7, T8>> all(
          Mono<? extends T1> p1,
          Mono<? extends T2> p2,
          Mono<? extends T3> p3,
          Mono<? extends T4> p4,
          Mono<? extends T5> p5,
          Mono<? extends T6> p6,
          Mono<? extends T7> p7,
          Mono<? extends T8> p8) {
    return TpfPromise.warp(Mono.zip(p1, p2, p3, p4, p5, p6, p7, p8))
        .nextValue(
            tuple8 ->
                TpfTuples.of(
                    OptionalUtil.nullIfEmpty(tuple8.getT1()),
                    OptionalUtil.nullIfEmpty(tuple8.getT2()),
                    OptionalUtil.nullIfEmpty(tuple8.getT3()),
                    OptionalUtil.nullIfEmpty(tuple8.getT4()),
                    OptionalUtil.nullIfEmpty(tuple8.getT5()),
                    OptionalUtil.nullIfEmpty(tuple8.getT6()),
                    OptionalUtil.nullIfEmpty(tuple8.getT7()),
                    OptionalUtil.nullIfEmpty(tuple8.getT8())));
  }

  /**
   * Aggregate given monos into a new {@literal Mono} that will be fulfilled when all of the given
   * {@literal Monos} have produced an item, aggregating their values according to the provided
   * combinator function. An error or <strong>empty</strong> completion of any source will cause
   * other sources to be cancelled and the resulting Mono to immediately error or complete,
   * respectively.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/zipt.png"
   * alt="">
   *
   * <p>
   *
   * @param monos The monos to use.
   * @param combinator the function to transform the combined array into an arbitrary object.
   * @param <R> the combined result
   * @return a {@link Mono}.
   */
  public static <R> TpfPromise<R> all(
      final Iterable<? extends TpfPromise<?>> monos,
      Function<? super Object[], ? extends R> combinator) {
    return TpfPromise.warp(Mono.zip(monos, combinator));
  }

  /**
   * Let this {@link TpfPromise} complete then play another Mono.
   *
   * <p>In other words ignore element from this {@link TpfPromise} and transform its completion
   * signal into the emission and completion signal of a provided {@code Mono<V>}. Error signal is
   * replayed in the resulting {@code Mono<V>}.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/ignorethen1.png"
   * alt="">
   *
   * @param other a {@link TpfPromise} to emit from after termination
   * @param <V> the element type of the supplied Mono
   * @return a new {@link TpfPromise} that emits from the supplied {@link TpfPromise}
   */
  public <V> TpfPromise<V> justThen(Mono<V> other) {
    return TpfPromise.warp(mono.then(other));
  }

  /**
   * Let this {@link TpfPromise} complete then play another Mono.
   *
   * <p>In other words ignore element from this {@link TpfPromise} and transform its completion
   * signal into the emission and completion signal of a provided {@code Mono<V>}. Error signal is
   * replayed in the resulting {@code Mono<V>}.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/ignorethen1.png"
   * alt="">
   *
   * @param supplier a {@link TpfPromise} to emit from after termination
   * @param <V> the element type of the supplied Mono
   * @return a new {@link TpfPromise} that emits from the supplied {@link TpfPromise}
   */
  public <V> TpfPromise<V> justThen(Supplier<Mono<V>> supplier) {
    return justThen(TpfPromise.warpCallAble(supplier));
  }

  public TpfPromise<T> ifEmptyThen(Supplier<? extends Mono<? extends T>> supplier) {
    return ifEmptyThen(
        TpfPromise.warpCallback(
            vTpfPromiseSink ->
                supplier
                    .get()
                    .subscribe(rel -> vTpfPromiseSink.success(rel), vTpfPromiseSink::error)));
  }

  /**
   * Fallback to an alternative {@link TpfPromise} if this mono is completed without data
   *
   * <p><img class="marble" src="doc-files/marbles/switchIfEmptyForMono.svg" alt="">
   *
   * @param alternate the alternate mono if this TpfPromise is empty
   * @return a {@link TpfPromise} falling back upon source completing without elements
   */
  public final TpfPromise<T> ifEmptyThen(Mono<? extends T> alternate) {
    return TpfPromise.warp(mono.switchIfEmpty(alternate));
  }

  @Override
  public void subscribe(CoreSubscriber<? super T> actual) {
    mono.subscribe(actual);
  }

  /**
   * Transform the item emitted by this {@link TpfPromise} asynchronously, returning the value
   * emitted by another {@link TpfPromise} (possibly changing the value type).
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/then.png"
   * alt="">
   *
   * <p>
   *
   * @param transformer the function to dynamically bind a new {@link TpfPromise}
   * @param <R> the result type bound
   * @return a new {@link TpfPromise} with an asynchronously mapped value.
   */
  public final <R> TpfPromise<R> nextDo(
      FunctionWithException<? super T, ? extends Mono<? extends R>> transformer) {
    Function<? super T, ? extends Mono<? extends R>> warp =
        new Function<T, Mono<? extends R>>() {
          @Override
          public Mono<? extends R> apply(T t) {
            if (t == Optional.empty()) {
              t = null;
            }
            try {
              return transformer.apply(t);
            } catch (Throwable e) {
              return Mono.error(e);
            }
          }
        };

    return TpfPromise.warp(mono.flatMap(warp));
  }

  /**
   * Subscribe to a fallback publisher when any error occurs, using a function to choose the
   * fallback depending on the error.
   *
   * <p><img class="marble" src="doc-files/marbles/onErrorResumeForMono.svg" alt="">
   *
   * @param fallback the function to choose the fallback to an alternative {@link TpfPromise}
   * @return a {@link TpfPromise} falling back upon source onError
   */
  public final TpfPromise<T> onErrorContinue(
      FunctionWithException<? super Throwable, ? extends Mono<? extends T>> fallback) {
    Function<? super Throwable, ? extends Mono<? extends T>> warp =
        (Function<? super Throwable, Mono<? extends T>>)
            t -> {
              try {
                return fallback.apply(t);
              } catch (Throwable e) {
                return Mono.error(e);
              }
            };

    return TpfPromise.warp(mono.onErrorResume(warp));
  }

  /**
   * 切换到当前队列执行后续流程
   *
   * @return 异步执行器 {@link TpfPromise}
   */
  public final TpfPromise<T> switchToCurrentThread() {
    CallQueue callQueue = CallQueueMgr.getInstance().getLocalQueue();
    return switchToThread(callQueue);
  }

  /**
   * 切换到返回结果对象所在的执行队列
   *
   * @return 异步执行器 {@link TpfPromise}
   */
  public final TpfPromise<T> switchToResultCallQueue() {
    return nextDo(
        result -> {
          TpfPromise<T> rel = TpfPromise.result(result);
          if (result == null) {
            log.info("switchToResultCallQueue relult is null keep current thread");
            return rel;
          }
          CallQueueProducer callQueueProducer = (CallQueueProducer) result;
          return rel.switchToThread(callQueueProducer.getQueueId());
        });
  }

  /**
   * 切换到目标对象所在的执行队列
   *
   * @param producer 目标对象 {@link CallQueueProducer}
   * @return 异步执行器 {@link TpfPromise}
   */
  public final TpfPromise<T> switchToTargetThread(CallQueueProducer producer) {
    if (producer == null) {
      return this;
    }
    return switchToThread(producer.getQueueId());
  }

  /**
   * 切换到hash code 对应的执行队列 根据 hash code 来摸执行队列数量，取得一个队列ID
   *
   * @param hashCode hash code
   * @return 异步执行器 {@link TpfPromise}
   */
  public final TpfPromise<T> switchToThreadByHashCode(int hashCode) {
    CallQueue callQueue = CallQueueMgr.getInstance().getQueueByHashCode(hashCode);
    return switchToThread(callQueue);
  }

  /**
   * 切换到目标ID执行队列
   *
   * @param callQueueId 目标ID
   * @return 异步执行器 {@link TpfPromise}
   */
  public final TpfPromise<T> switchToThread(int callQueueId) {
    CallQueue callQueue = CallQueueMgr.getInstance().getQueue(callQueueId);
    return switchToThread(callQueue);
  }

  /**
   * 切换到指定的执行队列
   *
   * @param callQueue 目标队列 {@link CallQueue}
   * @return a new {@link TpfPromise}
   */
  public final TpfPromise<T> switchToThread(CallQueue callQueue) {
    return nextDo(
        rel -> {
          if (callQueue != CallQueueMgr.getInstance().getLocalQueue()) {
            return TpfPromise.warpCallback(
                rTpfPromiseSink -> {
                  AbstractBaseEvent event =
                      new AbstractBaseEvent() {
                        @Override
                        public void prosess() {
                          rTpfPromiseSink.success(rel);
                        }
                      };
                  callQueue.addEvent(event);
                });
          } else {
            return TpfPromise.result(rel);
          }
        });
  }

  /**
   * Transform the item emitted by this {@link TpfPromise} by applying a synchronous function to it.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/map1.png"
   * alt="">
   *
   * <p>
   *
   * @param mapper the synchronous transforming {@link Function}
   * @param <R> the transformed type
   * @return a new {@link TpfPromise}
   */
  public final <R> TpfPromise<R> nextValue(FunctionWithException<? super T, ? extends R> mapper) {
    Function<? super T, ? extends TpfPromise<R>> warp =
        (Function<T, TpfPromise<R>>)
            t -> {
              try {
                if (t == Optional.empty()) {
                  t = null;
                }
                R ret = null;
                ret = mapper.apply(t);
                if (ret == null) {
                  return TpfPromise.result((R) Optional.empty());
                }
                return TpfPromise.result(ret);
              } catch (Throwable e) {
                return TpfPromise.error(e);
              }
            };
    return TpfPromise.warp(mono.flatMap(warp));
  }

  /**
   * Add behavior triggered when the {@link TpfPromise} completes successfully.
   *
   * <ul>
   *   <li>null : completed without data
   *   <li>T: completed with data
   * </ul>
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/doonsuccess.png"
   * alt="">
   *
   * <p>
   *
   * @param onSuccess the callback to call on, argument is null if the {@link TpfPromise} completes
   *     without data {@link Subscriber#onNext} or {@link Subscriber#onComplete} without preceding
   *     {@link Subscriber#onNext}
   * @return a new {@link TpfPromise}
   */
  public final TpfPromise<T> consumerValue(ConsumerWithException<? super T> onSuccess) {
    Consumer<? super T> warp =
        (Consumer<T>)
            t -> {
              if (t != null) {
                if (t == Optional.empty()) {
                  t = null;
                }
                try {
                  onSuccess.accept(t);
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              }
            };
    return TpfPromise.warp(mono.doOnSuccess(warp));
  }

  /**
   * Add behavior triggered when the {@link TpfPromise} completes successfully.
   *
   * <ul>
   *   <li>null : completed without data
   *   <li>T: completed with data
   * </ul>
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/doonsuccess.png"
   * alt="">
   *
   * <p>
   *
   * @param onSuccess the callback to call on, argument is null if the {@link TpfPromise} completes
   *     without data {@link Subscriber#onNext} or {@link Subscriber#onComplete} without preceding
   *     {@link Subscriber#onNext}
   * @return a new {@link TpfPromise}
   */
  public final TpfPromise<T> consumerEmpty(Runnable onSuccess) {
    Consumer<? super T> warp =
        new Consumer<T>() {
          @Override
          public void accept(T t) {
            if (t == null) {
              onSuccess.run();
            }
          }
        };
    return TpfPromise.warp(mono.doOnSuccess(warp));
  }

  /**
   * Add behavior triggered when the {@link TpfPromise} completes with an error.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/doonerror1.png"
   * alt="">
   *
   * <p>
   *
   * @param onError the error callback to call on {@link Subscriber#onError(Throwable)}
   * @return a new {@link TpfPromise}
   */
  public final TpfPromise<T> catchError(Consumer<? super Throwable> onError) {
    return TpfPromise.warp(mono.doOnError(onError));
  }

  /**
   * Add behavior triggering <strong>after</strong> the {@link TpfPromise} terminates for any
   * reason, including cancellation. The terminating event ({@link SignalType#ON_COMPLETE}, {@link
   * SignalType#ON_ERROR} and {@link SignalType#CANCEL}) is passed to the consumer, which is
   * executed after the signal has been passed downstream.
   *
   * <p>Note that the fact that the signal is propagated downstream before the callback is executed
   * means that several doFinally in a row will be executed in <strong>reverse order</strong>. If
   * you want to assert the execution of the callback please keep in mind that the Mono will
   * complete before it is executed, so its effect might not be visible immediately after eg. a
   * {@link #block()}.
   *
   * @param onFinally the callback to execute after a terminal signal (complete, error or cancel)
   * @return an observed {@link TpfPromise}
   */
  public final TpfPromise<T> finallySignal(Consumer<SignalType> onFinally) {
    return TpfPromise.warp(mono.doFinally(onFinally));
  }

  /**
   * Subscribe to this {@link TpfPromise} and request unbounded demand.
   *
   * <p>This version doesn't specify any consumption behavior for the events from the chain,
   * especially no error handling, so other variants should usually be preferred.
   *
   * <p><img width="500"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/unbounded1.png"
   * alt="">
   *
   * <p>
   *
   * @return a new {@link Disposable} that can be used to cancel the underlying {@link Subscription}
   */
  public final Disposable start() {
    return mono.subscribe();
  }

  /**
   * Subscribe a {@link Consumer} to this {@link TpfPromise} that will consume all the sequence. It
   * will request an unbounded demand ({@code Long.MAX_VALUE}).
   *
   * <p>For a passive version that observe and forward incoming data see {@link
   * #doOnNext(Consumer)}.
   *
   * <p>Keep in mind that since the sequence can be asynchronous, this will immediately return
   * control to the calling thread. This can give the impression the consumer is not invoked when
   * executing in a main thread or a unit test for instance.
   *
   * <p><img class="marble"
   * src="https://raw.githubusercontent.com/reactor/reactor-core/v3.1.3.RELEASE/src/docs/marble/subscribe1.png"
   * alt="">
   *
   * @param consumer the consumer to invoke on each value (onNext signal)
   * @return a new {@link Disposable} that can be used to cancel the underlying {@link Subscription}
   */
  public final Disposable start(Consumer<? super T> consumer) {
    Consumer<? super T> warp =
        new Consumer<T>() {
          final Consumer<? super T> param = consumer;

          @Override
          public void accept(T t) {
            if (t == Optional.empty()) {
              t = null;
            }
            param.accept(t);
          }
        };
    return mono.subscribe(warp);
  }

  /**
   * Set a Scheduler on which to execute the delays computed by the exponential backoff strategy.
   * for fixed delays (min backoff equals max backoff, no jitter), given a maximum number of retry
   * attempts and the fixed Duration for the backoff. Note that calling
   * RetryBackoffSpec.minBackoff(Duration) or RetryBackoffSpec.maxBackoff(Duration) would switch
   * back to an exponential backoff strategy.
   *
   * @param retryCount – the maximum number of retry attempts to allow
   * @param retryDelaySecond – retryDelaySecond of the fixed delays
   * @param exception – the exception can be retried
   * @return the fixed delays spec for further configuration
   */
  public final TpfPromise<T> retry(
      int retryCount, int retryDelaySecond, Class<? extends Throwable> exception) {
    Predicate<Throwable> retryPredicate = matchRetryFun.apply(retryCount, exception);
    CallQueue callQueue = CallQueueMgr.getInstance().getLocalQueue();
    return TpfPromise.warp(
        mono.retryWhen(
            Retry.fixedDelay(retryCount, Duration.ofSeconds(retryDelaySecond))
                .scheduler(Schedulers.single())
                .filter(retryPredicate)
                .doBeforeRetry(
                    retrySignal ->
                        log.warn(
                            "retry on the exception_{} starting , times_{}",
                            retrySignal.failure(),
                            retrySignal.totalRetries() + 1))
                .doAfterRetryAsync(
                    retrySignal -> {
                      if (callQueue == null) {
                        return TpfPromise.resultVoid();
                      }
                      return TpfPromise.warpCallback(
                          sink ->
                              CallQueueMgr.getInstance().task(sink::success, callQueue.getId()));
                    })));
  }

  /**
   * Set a Scheduler on which to execute the delays computed by the exponential backoff strategy.
   * for fixed delays (min backoff equals max backoff, no jitter), given a maximum number of retry
   * attempts and the fixed Duration for the backoff. Note that calling
   * RetryBackoffSpec.minBackoff(Duration) or RetryBackoffSpec.maxBackoff(Duration) would switch
   * back to an exponential backoff strategy.
   *
   * @param retryCount – the maximum number of retry attempts to allow
   * @param duration – the Duration of the fixed delays
   * @param exceptions – the predicate to filter which exceptions can be retried
   * @return the fixed delays spec for further configuration
   */
  public final TpfPromise<T> retry(
      int retryCount, Duration duration, Class<? extends Throwable>... exceptions) {
    Predicate<Throwable> retryPredicate =
        anyMatchRetryFun.apply(retryCount, Arrays.asList(exceptions));
    CallQueue callQueue = CallQueueMgr.getInstance().getLocalQueue();
    return TpfPromise.warp(
        mono.retryWhen(
            Retry.fixedDelay(retryCount, duration)
                .scheduler(Schedulers.single())
                .filter(retryPredicate)
                .doBeforeRetry(
                    retrySignal ->
                        log.warn(
                            "retry on the exception_{} starting , times_{}",
                            retrySignal.failure(),
                            retrySignal.totalRetries() + 1))
                .doAfterRetryAsync(
                    retrySignal -> {
                      if (callQueue == null) {
                        return TpfPromise.resultVoid();
                      }
                      return TpfPromise.warpCallback(
                          sink ->
                              CallQueueMgr.getInstance().task(sink::success, callQueue.getId()));
                    })));
  }
}
