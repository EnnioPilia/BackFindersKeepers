import { Component, forwardRef, Input } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';  // <-- Import FormsModule

export interface SelectOption {
  label: string;
  value: any;
}

@Component({
  selector: 'app-shared-select',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './shared-select.component.html',
  styleUrls: ['./shared-select.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SharedSelectComponent),
      multi: true
    }
  ]
})
export class SharedSelectComponent implements ControlValueAccessor {
  @Input() label: string = '';
  @Input() options: SelectOption[] = [];
  @Input() invalid: boolean = false;
  @Input() errorMessage: string = '';

  value: any = null;
  disabled: boolean = false;

  selectId = `shared-select-${Math.random().toString(36).substr(2, 9)}`;

  // Callbacks
  onChange = (_: any) => {};
  onTouched = () => {};

  writeValue(value: any): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }
}
