import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectCardDetailComponent } from './object-card-detail.component';

describe('ObjectCardDetailComponent', () => {
  let component: ObjectCardDetailComponent;
  let fixture: ComponentFixture<ObjectCardDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectCardDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObjectCardDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
