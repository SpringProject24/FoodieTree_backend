<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>FoodieTree</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
          crossorigin="anonymous">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"
          integrity="sha512-SnH5WK+bZxgPHs44uWIX+LLJAJ9/2PkPKZ5QiAj6Ta86w+fsb2TkcmfRyVX3pBnMFcV7oQPJkl9QevSCWr3W6A=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
    <link rel="stylesheet" href="/assets/css/product/main.css">

    <style>
      .rectangle {
        width: 100px;
        height: 100px;
        background-color: red;
      }

      .circle {
        width: 100px;
        height: 100px;
        background-color: blue;
        border-radius: 50%;
      }

      .triangle {
        width: 0;
        height: 0;
        border-left: 50px solid transparent;
        border-right: 50px solid transparent;
        border-bottom: 100px solid green;
      }
    </style>
  </head>
  <body>
  <c:forEach var="item" items="${productTotal.productDtoList}">
      <div>
        ${item.storeName}
      </div>
  </c:forEach>
  <c:forEach var="item" items="${productTotal.preferredArea}">
      <div>
        ${item}
      </div>
  </c:forEach>
  <c:forEach var="item" items="${productTotal.preferredFood}">
    <div>
      ${item}
    </div>
</c:forEach>
    <header>
      <div class="container">
        <div class="logo-wrapper">
          <div class="logo"><h1>FoodieTree</h1></div>
        </div>
        <div class="input-wrapper">
          <button><i class="fa-solid fa-search"></i></button>
          <input type="text" placeholder="Search" />
        </div>
        <ul class="profile-wrapper">
          <li>
            <a href="#" class="profile img-box">
              <img src="/assets/img/western.jpg" alt="" />
            </a>
          </li>
          <li>
            <a href="/customer/mypage">마이페이지</a>
          </li>
          <li>
            <a href="/customer/signout">로그아웃</a>
          </li>
        </ul>
      </div>
    </header>

    <section class="container category">
      <div class="swiper category-list">
        <div class="swiper-wrapper">
          <div class="swiper-slide">
            <div class="item">
              <div class="img-box">
                <img src="/assets/img/western.jpg" alt="" />
              </div>
              <p>양식</p>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="img-box">
                <img src="/assets/img/cafe.jpg" alt="" />
              </div>
              <p>카페</p>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="img-box">
                <img src="/assets/img/dessert.jpg" alt="" />
              </div>
              <p>디저트</p>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="img-box">
                <img src="/assets/img/japanese.jpg" alt="" />
              </div>
              <p>일식</p>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="img-box">
                <img src="/assets/img/chinese.jpg" alt="" />
              </div>
              <p>중식</p>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="img-box">
                <img src="/assets/img/korean.jpg" alt="" />
              </div>
              <p>한식</p>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="img-box">
                <img src="/assets/img/etc.jpg" alt="" />
              </div>
              <p>기타</p>
            </div>
          </div>
        </div>
      </div>
    </section>
    <section class="container category">
      <div class="swiper myswiper1">
        <div class="title-wrapper">
          <h2>선호하는 음식</h2>
          <div class="swiper-button-prev">
            <i class="fa-solid fa-arrow-left"></i>
          </div>
          <div class="swiper-button-next">
            <i class="fa-solid fa-arrow-right"></i>
          </div>
        </div>
        <div class="swiper-wrapper">
          <div class="swiper-slide">
            <c:forEach var="item" items="${categoryByFood}">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/western.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름 : ${item.storeName}</h3>
                <p>픽업 시간 : ${item.pickupTime}</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격 : ${item.price}</p>
                </div>
              </div>
            </c:forEach>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/cafe.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/dessert.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/japanese.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/chinese.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/korean.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/etc.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="container category">
      <div class="swiper myswiper2">
        <div class="title-wrapper">
          <h2>선호하는 지역</h2>
          <div class="btn-wrapper">
            <div class="swiper-button-prev-b circle">
              <i class="fa-solid fa-arrow-left"></i>
            </div>
            <div class="swiper-button-next-b circle">
              <i class="fa-solid fa-arrow-right"></i>
            </div>
          </div>
        </div>
        <div class="swiper-wrapper">
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/western.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/cafe.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/dessert.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/japanese.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/chinese.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/korean.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/etc.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="rectangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="triangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="rectangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="triangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
        </div>
      </div>
    </section>

    <section class="container category">
      <div class="swiper myswiper3">
        <div class="title-wrapper">
          <h2>최애 가게</h2>
          <div class="btn-wrapper">
            <div class="swiper-button-prev-b circle">
              <i class="fa-solid fa-arrow-left"></i>
            </div>
            <div class="swiper-button-next-b circle">
              <i class="fa-solid fa-arrow-right"></i>
            </div>
          </div>
        </div>
        <div class="swiper-wrapper">
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/western.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/cafe.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/dessert.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/japanese.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/chinese.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/korean.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="item">
              <div class="store-img-box">
                <img src="/assets/img/etc.jpg" alt="" />
              </div>
              <div class="store-info">
                <h3>가게 이름</h3>
                <p>픽업 시간</p>
                <div class="wrapper">
                  <p>평점 / 수량</p>
                  <p>가격</p>
                </div>
              </div>
            </div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="rectangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="triangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="rectangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
          <div class="swiper-slide">
            <div class="triangle"></div>
          </div>
          <div class="swiper-slide">
            <div class="circle"></div>
          </div>
        </div>
    </div>
</section>
<!-- Button trigger modal -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
    Launch demo modal
</button>

<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                ...
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
            </div>
        </div>
    </div>
</div>

    <footer>
      <div class="container">
        <p>&copy; 2024 FoodieTree. All rights reserved.</p>
      </div>
    </footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<script>
  new Swiper('.category-list', {
    slidesPerView: 7,
    centralDirOffset: 1,
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
  });

  new Swiper('.myswiper1', {
    spaceBetween: 20,
    slidesPerView: 4,
    slidesPerGroup: 4,
    loop: true,
    speed: 1200,
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev',
    },
  });

  new Swiper('.myswiper2', {
    spaceBetween: 20,
    slidesPerView: 5,
    slidesPerGroup: 5,
    speed: 1200,
    navigation: {
      nextEl: '.swiper-button-next-b',
      prevEl: '.swiper-button-prev-b',
    }
  });

  new Swiper('.myswiper3', {
    spaceBetween: 20,
    slidesPerView: 5,
    slidesPerGroup: 5,
    speed: 1200,
    navigation: {
      nextEl: '.swiper-button-next-b',
      prevEl: '.swiper-button-prev-b',
    }
  });

</script>
</body>
</html>
