import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectsCardComponent } from './objects-card.component';

describe('ObjectsCardComponent', () => {
  let component: ObjectsCardComponent;
  let fixture: ComponentFixture<ObjectsCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectsCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObjectsCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
