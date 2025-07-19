import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeLoginCardComponent } from './home-login-card.component';

describe('HomeLoginCardComponent', () => {
  let component: HomeLoginCardComponent;
  let fixture: ComponentFixture<HomeLoginCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeLoginCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomeLoginCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
